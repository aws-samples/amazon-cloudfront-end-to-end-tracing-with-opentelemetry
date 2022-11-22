# Preparing infrastructure


<!-- BEGIN_TF_DOCS -->
## Requirements

| Name | Version |
|------|---------|
| <a name="requirement_aws"></a> [aws](#requirement\_aws) | ~> 4.12.0 |
| <a name="requirement_helm"></a> [helm](#requirement\_helm) | ~> 2.6.0 |

## Providers

| Name | Version |
|------|---------|
| <a name="provider_aws"></a> [aws](#provider\_aws) | 4.12.1 |
| <a name="provider_helm"></a> [helm](#provider\_helm) | 2.6.0 |
| <a name="provider_kubernetes"></a> [kubernetes](#provider\_kubernetes) | 2.11.0 |
| <a name="provider_random"></a> [random](#provider\_random) | 3.3.2 |

## Modules

| Name | Source | Version |
|------|--------|---------|
| <a name="module_eks"></a> [eks](#module\_eks) | terraform-aws-modules/eks/aws | 18.20.5 |
| <a name="module_lambda_edge"></a> [lambda\_edge](#module\_lambda\_edge) | ./modules/lambda-edge | n/a |
| <a name="module_launch-templates-on-demand"></a> [launch-templates-on-demand](#module\_launch-templates-on-demand) | ./modules/launch-templates | n/a |
| <a name="module_rds"></a> [rds](#module\_rds) | terraform-aws-modules/rds-aurora/aws | 7.1.0 |

## Resources

| Name | Type |
|------|------|
| [aws_cloudfront_distribution.e2e_tracing](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/cloudfront_distribution) | resource |
| [aws_cloudwatch_log_group.msk_broker_logs](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/cloudwatch_log_group) | resource |
| [aws_db_subnet_group.backing-private](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/db_subnet_group) | resource |
| [aws_default_network_acl.dev_default](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/default_network_acl) | resource |
| [aws_eip.nat](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/eip) | resource |
| [aws_elasticache_replication_group.elasticache](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_replication_group) | resource |
| [aws_elasticache_subnet_group.elasticache](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_subnet_group) | resource |
| [aws_iam_instance_profile.ssm_instance_profile](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/iam_instance_profile) | resource |
| [aws_iam_policy.this](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/iam_policy) | resource |
| [aws_iam_role.ssm_instance_profile](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/iam_role) | resource |
| [aws_iam_role.this](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/iam_role) | resource |
| [aws_iam_role_policy_attachment.this](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/iam_role_policy_attachment) | resource |
| [aws_instance.web_server_01](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/instance) | resource |
| [aws_internet_gateway.this](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/internet_gateway) | resource |
| [aws_kms_key.eks](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/kms_key) | resource |
| [aws_lb.alb_origin](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb) | resource |
| [aws_lb.k8s_ingress](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb) | resource |
| [aws_lb_listener.delivery_service](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_listener) | resource |
| [aws_lb_listener.nginx_80](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_listener) | resource |
| [aws_lb_listener.nginx_otel_http](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_listener) | resource |
| [aws_lb_listener.order_service](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_listener) | resource |
| [aws_lb_listener.otel_collector_grpc](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_listener) | resource |
| [aws_lb_listener.otel_collector_http](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_listener) | resource |
| [aws_lb_target_group.delivery_service](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_target_group) | resource |
| [aws_lb_target_group.nginx](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_target_group) | resource |
| [aws_lb_target_group.nginx_otel_http](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_target_group) | resource |
| [aws_lb_target_group.order_service](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_target_group) | resource |
| [aws_lb_target_group.otel_collector_grpc](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_target_group) | resource |
| [aws_lb_target_group.otel_collector_http](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_target_group) | resource |
| [aws_lb_target_group_attachment.nginx](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_target_group_attachment) | resource |
| [aws_lb_target_group_attachment.nginx_otel_http](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/lb_target_group_attachment) | resource |
| [aws_msk_cluster.msk-cluster](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/msk_cluster) | resource |
| [aws_msk_configuration.initial_config](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/msk_configuration) | resource |
| [aws_nat_gateway.this](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/nat_gateway) | resource |
| [aws_opensearch_domain.this](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/opensearch_domain) | resource |
| [aws_route_table.private](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/route_table) | resource |
| [aws_route_table.public](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/route_table) | resource |
| [aws_route_table_association.backing-private](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/route_table_association) | resource |
| [aws_route_table_association.eks-private](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/route_table_association) | resource |
| [aws_route_table_association.public](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/route_table_association) | resource |
| [aws_s3_bucket.e2e-tracing-opentelemetry](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/s3_bucket) | resource |
| [aws_security_group.allow_inbound_http](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group) | resource |
| [aws_security_group.allow_inbound_otlp_http](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group) | resource |
| [aws_security_group.allow_msk](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group) | resource |
| [aws_security_group.allow_nginx](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group) | resource |
| [aws_security_group.allow_outbound_traffic](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group) | resource |
| [aws_security_group.allow_postgres](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group) | resource |
| [aws_security_group.elasticache-cluster-sg](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group) | resource |
| [aws_ssm_parameter.opentelemetry_collector_hostname](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/ssm_parameter) | resource |
| [aws_ssm_parameter.opentelemetry_collector_otlp_http_port](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/ssm_parameter) | resource |
| [aws_ssm_parameter.trace_sampling_rate](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/ssm_parameter) | resource |
| [aws_subnet.backing-private](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/subnet) | resource |
| [aws_subnet.eks-private](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/subnet) | resource |
| [aws_subnet.public](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/subnet) | resource |
| [aws_vpc.this](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/vpc) | resource |
| [helm_release.aws-lb-controller](https://registry.terraform.io/providers/hashicorp/helm/latest/docs/resources/release) | resource |
| [helm_release.data-prepper](https://registry.terraform.io/providers/hashicorp/helm/latest/docs/resources/release) | resource |
| [helm_release.e2e-sample-delivery](https://registry.terraform.io/providers/hashicorp/helm/latest/docs/resources/release) | resource |
| [helm_release.e2e-sample-order](https://registry.terraform.io/providers/hashicorp/helm/latest/docs/resources/release) | resource |
| [helm_release.opentelemetry-collector](https://registry.terraform.io/providers/hashicorp/helm/latest/docs/resources/release) | resource |
| [kubernetes_manifest.delivery-service-tgb](https://registry.terraform.io/providers/hashicorp/kubernetes/latest/docs/resources/manifest) | resource |
| [kubernetes_manifest.opentelemetry-collector-grpc-tgb](https://registry.terraform.io/providers/hashicorp/kubernetes/latest/docs/resources/manifest) | resource |
| [kubernetes_manifest.opentelemetry-collector-http-tgb](https://registry.terraform.io/providers/hashicorp/kubernetes/latest/docs/resources/manifest) | resource |
| [kubernetes_manifest.order-service-tgb](https://registry.terraform.io/providers/hashicorp/kubernetes/latest/docs/resources/manifest) | resource |
| [kubernetes_service_account.this](https://registry.terraform.io/providers/hashicorp/kubernetes/latest/docs/resources/service_account) | resource |
| [random_password.master](https://registry.terraform.io/providers/hashicorp/random/latest/docs/resources/password) | resource |
| [aws_ami.amazon_linux_2](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/ami) | data source |
| [aws_caller_identity.current](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/caller_identity) | data source |
| [aws_cloudfront_cache_policy.cache_disabled](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/cloudfront_cache_policy) | data source |
| [aws_eks_cluster.cluster](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/eks_cluster) | data source |
| [aws_eks_cluster_auth.cluster](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/eks_cluster_auth) | data source |
| [aws_iam_policy_document.this](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/iam_policy_document) | data source |
| [aws_region.current](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/data-sources/region) | data source |

## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| <a name="input_azs"></a> [azs](#input\_azs) | n/a | `list` | <pre>[<br>  "ap-northeast-2a",<br>  "ap-northeast-2c"<br>]</pre> | no |
| <a name="input_cidr"></a> [cidr](#input\_cidr) | n/a | `string` | `"10.0.0.0/16"` | no |
| <a name="input_delivery_sample_image_repository"></a> [delivery\_sample\_image\_repository](#input\_delivery\_sample\_image\_repository) | n/a | `any` | n/a | yes |
| <a name="input_delivery_sample_image_tag"></a> [delivery\_sample\_image\_tag](#input\_delivery\_sample\_image\_tag) | n/a | `string` | `"latest"` | no |
| <a name="input_eks_cluster_name"></a> [eks\_cluster\_name](#input\_eks\_cluster\_name) | n/a | `string` | `"eks-sample"` | no |
| <a name="input_enable_irsa"></a> [enable\_irsa](#input\_enable\_irsa) | Indicates whether or not the Amazon EKS public API server endpoint is enabled. Default to AWS EKS resource and it is true | `bool` | `true` | no |
| <a name="input_irsa_values"></a> [irsa\_values](#input\_irsa\_values) | n/a | `any` | n/a | yes |
| <a name="input_kubernetes_version"></a> [kubernetes\_version](#input\_kubernetes\_version) | Desired Kubernetes master version. If you do not specify a value, the latest available version is used | `string` | `"1.21"` | no |
| <a name="input_on_demand_node_group_name"></a> [on\_demand\_node\_group\_name](#input\_on\_demand\_node\_group\_name) | AWS eks managed node group name | `string` | `"mg-m5-on-demand"` | no |
| <a name="input_opensearch_domain"></a> [opensearch\_domain](#input\_opensearch\_domain) | n/a | `string` | `"opensearch"` | no |
| <a name="input_opensearch_instance_type"></a> [opensearch\_instance\_type](#input\_opensearch\_instance\_type) | n/a | `string` | `"r5.large.search"` | no |
| <a name="input_opensearch_master_password"></a> [opensearch\_master\_password](#input\_opensearch\_master\_password) | n/a | `string` | `"Master!234"` | no |
| <a name="input_opensearch_master_username"></a> [opensearch\_master\_username](#input\_opensearch\_master\_username) | n/a | `string` | `"master"` | no |
| <a name="input_order_sample_image_repository"></a> [order\_sample\_image\_repository](#input\_order\_sample\_image\_repository) | n/a | `any` | n/a | yes |
| <a name="input_order_sample_image_tag"></a> [order\_sample\_image\_tag](#input\_order\_sample\_image\_tag) | n/a | `string` | `"latest"` | no |
| <a name="input_region"></a> [region](#input\_region) | n/a | `string` | `"ap-northeast-2"` | no |
| <a name="input_trace_sampling_rate"></a> [trace\_sampling\_rate](#input\_trace\_sampling\_rate) | Trace sampling rate between 0 and 100 (default: always) | `number` | `100` | no |

## Outputs

| Name | Description |
|------|-------------|
| <a name="output_additional_cluster_endpoints"></a> [additional\_cluster\_endpoints](#output\_additional\_cluster\_endpoints) | A map of additional cluster endpoints and their attributes |
| <a name="output_aws-nginx-ip"></a> [aws-nginx-ip](#output\_aws-nginx-ip) | Define an output value of the IP of the EC2 instance |
| <a name="output_bootstrap_brokers_tls"></a> [bootstrap\_brokers\_tls](#output\_bootstrap\_brokers\_tls) | TLS connection host:port pairs |
| <a name="output_cluster_instances"></a> [cluster\_instances](#output\_cluster\_instances) | A map of cluster instances and their attributes |
| <a name="output_cluster_name"></a> [cluster\_name](#output\_cluster\_name) | Kubernetes Cluster Name |
| <a name="output_cluster_oidc_url"></a> [cluster\_oidc\_url](#output\_cluster\_oidc\_url) | The URL on the EKS cluster OIDC Issuer |
| <a name="output_configure_kubectl"></a> [configure\_kubectl](#output\_configure\_kubectl) | Configure kubectl: make sure you're logged in with the correct AWS profile and run the following command to update your kubeconfig |
| <a name="output_oidc_provider_arn"></a> [oidc\_provider\_arn](#output\_oidc\_provider\_arn) | The ARN of the OIDC Provider if `enable_irsa = true`. |
| <a name="output_opensearch_endpoint"></a> [opensearch\_endpoint](#output\_opensearch\_endpoint) | n/a |
| <a name="output_opentelemetry_collector_hostname"></a> [opentelemetry\_collector\_hostname](#output\_opentelemetry\_collector\_hostname) | n/a |
| <a name="output_rds_cluster_database_name"></a> [rds\_cluster\_database\_name](#output\_rds\_cluster\_database\_name) | Name for an automatically created database on cluster creation |
| <a name="output_rds_cluster_endpoint"></a> [rds\_cluster\_endpoint](#output\_rds\_cluster\_endpoint) | The cluster endpoint |
| <a name="output_rds_cluster_id"></a> [rds\_cluster\_id](#output\_rds\_cluster\_id) | The ID of the cluster |
| <a name="output_rds_cluster_master_password"></a> [rds\_cluster\_master\_password](#output\_rds\_cluster\_master\_password) | The master password |
| <a name="output_rds_cluster_master_username"></a> [rds\_cluster\_master\_username](#output\_rds\_cluster\_master\_username) | The master username |
| <a name="output_rds_cluster_port"></a> [rds\_cluster\_port](#output\_rds\_cluster\_port) | The port |
| <a name="output_rds_cluster_reader_endpoint"></a> [rds\_cluster\_reader\_endpoint](#output\_rds\_cluster\_reader\_endpoint) | The cluster reader endpoint |
| <a name="output_rds_cluster_resource_id"></a> [rds\_cluster\_resource\_id](#output\_rds\_cluster\_resource\_id) | The Resource ID of the cluster |
| <a name="output_rds_security_group_id"></a> [rds\_security\_group\_id](#output\_rds\_security\_group\_id) | The security group ID of the cluster |
| <a name="output_zookeeper_connect_string"></a> [zookeeper\_connect\_string](#output\_zookeeper\_connect\_string) | n/a |
<!-- END_TF_DOCS -->