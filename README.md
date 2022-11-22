## Setting up end-to-end tracing that starts from Amazon CloudFront using OpenTelemetry

## Introduction
In this blog, you will learn how to setup an end-to-end tracing with [OpenTelemetry](https://opentelemetry.io/) that starts from Amazon CloudFront. An end-to-end tracing, or E2E tracing is a great tool to have in order to identify problems in a modern, distributed architecture. Although a tracing starts from Amazon CloudFront technically does not qualify as a true end-to-end tracing since it does not consider the things happen in the client, you can expand the concepts covered in this post and setup your own E2E tracing starting from your clients.  
  
Unlike Amazon API Gateway, Amazon CloudFront does not support tracing out of the box. But thanks to [CloudFront Function,](https://aws.amazon.com/lambda/edge/) Amazon CloudFront can send OpenTelemetry compatible message to a OpenTelemetry Collector and the traces can be aggregated in your preferred backend including but not limited to [Jaeger](https://www.jaegertracing.io/), [Zipkin](https://zipkin.io/), [Prometheus](https://prometheus.io/), Elasticsearch or [Amazon Opensearch](https://aws.amazon.com/opensearch-service/).  
  
In this post, we will be using [Amazon Opensearch](https://aws.amazon.com/opensearch-service/) as my backend to enjoy the benefit of [Opensearch Trace Analytics](https://docs.aws.amazon.com/opensearch-service/latest/developerguide/trace-analytics.html)for visualizing the traces. But you are free to use different backend of your choice.   
  
You can find all of the code and resources used throughout this post in [the associated GitHub repository](https://github.com/aws-samples/Load-testing-your-workload-running-on-Amazon-EKS-with-Locust).  
  

## Overview of solution
![](./overview.jpg)
For demonstration, we’ll build the above sample architecture. In an EKS cluster, we’ll host two microservices—the Upstream and Downstream services— to serve HTTP requests. The HTTP endpoints are exposed to the public via CloudFront. To trace user request, key architecture components are instrumented with OpenTelemetry.   
  
[OpenTelemetry](https://opentelemetry.io/docs/concepts/what-is-opentelemetry/) is a set of APIs, SDKs, tooling and integrations designed for telemetry data such as traces, metrics, and logs. It provides a common specification to generate and collect telemetry data in vendor-agnostic way. In this post, we use OpenTelemetry to instrument CloudFront, Nginx proxy, and microservices and construct a complete trace of user request starting from CloudFront.  
  
We can use the [OpenTelemetry Nginx module](https://github.com/open-telemetry/opentelemetry-cpp-contrib/tree/main/instrumentation/nginx) to instrument the Nginx proxy and the [OpenTelemetry Java Instrument Agent](https://github.com/open-telemetry/opentelemetry-java-instrumentation) to instrument the microservices (written in Java). However, there is no such implementation available for CloudFront. Instead, we can write custom logic in CloudFront Function to build trace context, propagate the context by adding request headers, and send the trace data to the OpenTelemetry Collector.  
  
The trace data collected from each component are all sent to the [OpenTelemetry Collector.](https://opentelemetry.io/docs/collector/) The Collector serves as a common facade to a tracing back-end service of your choice. In this blog, we’ve adopted Amazon OpenSearch with [Trace Analytics](https://docs.aws.amazon.com/opensearch-service/latest/developerguide/trace-analytics.html) as the tracing back-end. In addition, we use [Data Prepper](https://opensearch.org/docs/latest/clients/data-prepper/index/) to convert the OpenTelemetry-format trace data into the OpenSearch-compatible format.  
  
(Foot Note: Although we’ve adopted Amazon OpenSearch as the tracing back-end, the reader can choose from multiple open-source and commercial back-ends supported by the OpenTelemetry Collector. Choosing a collector-supported back-end would obviate the need for data conversion tool, such as Data Prepper.)  
  

## Groundwork - IaC

-   CloudFront, EKS, Kafka, RDS, Redis, Opensearch

## Walkthrough - Demo

-   Sample API invocation & trace view in the Trace Analytics
-   synchronous call (HTTP) between microservices
-   asynchronous call (Kafka) between microservices
-   Detailed description on the request flow & how tracing works in each component
-   각 항목에 관련 그림, 코드, 설정파일 추가

  
Below is the detailed description of how each request is traced:  

1.  In this architecture, CloudFront is the component that initiates tracing for each user request. The root span of the trace is opened by the Origin Request Lambda and closed by the Origin Response Lambda. The Origin Request Lambda opens the root span by generating trace context and attaching it to the request headers. The generated trace context includes trace and span IDs, start time, and sampling decision flag.  
    (Go through lambda code here)  
    (Show how trace context is attached to the request headers)  
    (Explain why sampling matters and how it’s implemented)
2.  The request is passed to the Nginx proxy hosted in an EC2 instance. The Nginx proxy is installed with the [OpenTelemetry Nginx Module](https://github.com/open-telemetry/opentelemetry-cpp-contrib/tree/main/instrumentation/nginx), which will enable automatic tracing at the proxy. The module recognizes the trace context in the request headers, and thus opens a child span of the previous span (i.e., creates another span ID and starts recording span time). The module updates the trace context in the request headers with the current span ID, and passes down the request to the Upstream microservice.  
    (Nginx Module 빌드 스크립트 파일 첨부 (깃헙 링크). 가능하면 모듈 바이너리 파일도 추가)  
    (Show related configuration?)
3.  The Upstream microservice handles the user request. Depending on the requested API, the Upstream service may call the Downstream service synchronously by HTTP or asynchronously via Amazon MSK. While processing the request, the microservices may query from Amazon ElastiCache and Amazon RDS. All these interactions during the request processing are automatically traced and recorded as sub-level spans by the [OpenTelemetry Java Agent](https://github.com/open-telemetry/opentelemetry-java-instrumentation).
4.  Just before the Upstream service finishes processing and returns a response, the OpenTelemetry Java Agent sends the trace data to the OpenTelemetry Collector. 
5.  The OpenTelemetry Collector serves as a vendor-agnostic telemetry data pipeline. We can configure in which format and protocol to injest telemetry data (“receivers”), how to process the data (“processors”), and in which format and endpoint to export the data (“exporters”).

1.  (Show the collector configuration file to display receivers, processors, exporters)
2.  In this post, we generate OpenTelemetry-format trace data, which can be ingested to the Collector via gRPC or HTTP protocol.
3.  (Explain the processors)
4.  The processed data are sent to the OpenTelemetry endpoint of the Data Prepper to be converted to the OpenSearch format.

7.  The Collector can be deployed in a number of different modes. It can be injected to the microservice pod as a sidecar, or it can be installed as a daemon set in each host. In this post, we installed the Collector as a deployment, a standalone Kubernetes service with replicas. Deployed as a service, the Collector can serve as a common egress point to process the data before sending to the tracing back-end.
8.  When returning the response, the OpenTelemetry Nginx module closes the span and sends the trace data to the OpenTelemetry Collector.
9.  When it’s triggered, the Origin Response Lambda receives the matching origin request event including request headers. Thus the Origin Response Lambda can recover trace context, measure the end time of the span, and send the root span data to the OpenTelemetry Collector.

마지막에 Trace Analytics 의 트레이스 화면 첨부 및 설명  
  

## Cleaning up

(Sean)  

## Conclusion
  
Readers will learn how to setup end-to-end tracing that starts from Amazon CloudFront using CloudFront Function, OpenTelemetry Collector, Data Prepper, Opensearch Trace Analytics.  
  
A Telco customer requested that they want to see traces starting from Amazon CloudFront in order to quickly identify bottlenecks.   
  
User wants to collect trace data that starts from Amazon CloudFront to identify bottlenecks or failure point so that they can respond quickly to a performance issue.  
  
Outlines how the components of the solution, CloudFront, CloudFront Function, OpenTelemetry Collector, Istio, EKS, Opensearch Cluster work together to populate traces in Opensearch Trace Analytics Dashboard.  
  
**Setup**  
  
Here I provide  

-   a link to a github repo that hosts CDK template.
-   Scripts to provision AWS resources

**OpenTelemetry** **Configuration**  
  
Here I describe manual configuration needed for OpenTelemtry wiring to make the demo work.  
  
**Validation**  
  
Setup CloudWatch Synthetic to generate mock traffic to CloudFront and check out the Traces being logged successfully in the Opensearch Dashboard.  
  

## Reference

-   OpenTelemetry Doc: [What is OpenTelemetry](https://opentelemetry.io/docs/concepts/what-is-opentelemetry/) 

-   [AWS Open Source Blog: Distributed tracing with OpenTelemetry](https://aws.amazon.com/blogs/opensource/distributed-tracing-with-opentelemetry/)
-   AWS Cloud Operations & Migrations Blog: [Build an observability solution using managed AWS services and the OpenTelemetry standard](https://aws.amazon.com/blogs/mt/build-an-observability-solution-using-managed-aws-services-and-the-opentelemetry-standard/)