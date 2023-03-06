FROM        gradle:7.6.1-jdk17-alpine AS build

WORKDIR     /home/demo

COPY        --chown=gradle:gradle . /home/demo
RUN         gradle clean build -x test --no-daemon

FROM        amazoncorretto:17-alpine

RUN         addgroup -S demo && adduser -S demo -G demo
USER        demo

ARG         JVM_OPS
ENV         JVM_OPS=${JVM_OPS}

COPY        --from=build /home/demo/build/libs/*.jar app.jar

ADD         --chown=demo:demo https://github.com/aws-observability/aws-otel-java-instrumentation/releases/latest/download/aws-opentelemetry-agent.jar /opt/aws-opentelemetry-agent.jar
ENV         JAVA_TOOL_OPTIONS=-javaagent:/opt/aws-opentelemetry-agent.jar

ENTRYPOINT  ["sh","-c","java $JVM_OPS -jar app.jar"]
