#output "cluster_oidc_url" {
#  description = "The URL on the EKS cluster OIDC Issuer"
#  value       = split("//", module.eks.cluster_oidc_issuer_url)[1]
#}

#output "oidc_provider_arn" {
#  description = "The ARN of the OIDC Provider if `enable_irsa = true`."
#  value       = module.eks.oidc_provider_arn
#}

#output "cluster_name" {
#  description = "Kubernetes Cluster Name"
#  value       = module.eks.cluster_id
#}

output "configure_kubectl" {
  description = "Configure kubectl: make sure you're logged in with the correct AWS profile and run the following command to update your kubeconfig"
  value       = "aws eks --region ${data.aws_region.current.id} update-kubeconfig --name ${module.eks.cluster_id}"
}

#output "zookeeper_connect_string" {
#  value = aws_msk_cluster.msk-cluster.zookeeper_connect_string
#}

#output "bootstrap_brokers_tls" {
#  description = "TLS connection host:port pairs"
#  value       = aws_msk_cluster.msk-cluster.bootstrap_brokers_tls
#}
# aws_rds_cluster
#output "rds_cluster_id" {
#  description = "The ID of the cluster"
#  value       = module.rds.cluster_id
#}

#output "rds_cluster_resource_id" {
#  description = "The Resource ID of the cluster"
#  value       = module.rds.cluster_resource_id
#}

#output "rds_cluster_endpoint" {
#  description = "The cluster endpoint"
#  value       = module.rds.cluster_endpoint
#}

#output "rds_cluster_reader_endpoint" {
#  description = "The cluster reader endpoint"
#  value       = module.rds.cluster_reader_endpoint
#}

#output "rds_cluster_database_name" {
#  description = "Name for an automatically created database on cluster creation"
#  value       = module.rds.cluster_database_name
#}

#output "rds_cluster_master_password" {
#  description = "The master password"
#  value       = module.rds.cluster_master_password
#  sensitive   = true
#}

#output "rds_cluster_port" {
#  description = "The port"
#  value       = module.rds.cluster_port
#}

#output "rds_cluster_master_username" {
#  description = "The master username"
#  value       = module.rds.cluster_master_username
#  sensitive   = true
#}

# aws_rds_cluster_instances
#output "cluster_instances" {
#  description = "A map of cluster instances and their attributes"
#  value       = module.rds.cluster_instances
#}

# aws_rds_cluster_endpoint
/*output "additional_cluster_endpoints" {
  description = "A map of additional cluster endpoints and their attributes"
  value       = module.rds.additional_cluster_endpoints
}*/

# aws_security_group
#output "rds_security_group_id" {
#  description = "The security group ID of the cluster"
#  value       = module.rds.security_group_id
#}

output "kibana_dashboard" {
  value = "https://${aws_opensearch_domain.this.endpoint}/_plugin/kibana"
}

output "kibana_dashboard_username" {
  value = var.opensearch_master_username
}

output "kibana_dashboard_password" {
  value = var.opensearch_master_password
}

output "cfn_distribution_domain_name" {
  value = aws_cloudfront_distribution.e2e_tracing.domain_name
}