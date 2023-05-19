output "configure_kubectl" {
  description = "Configure kubectl: make sure you're logged in with the correct AWS profile and run the following command to update your kubeconfig"
  value       = "aws eks --region ${data.aws_region.current.id} update-kubeconfig --name ${module.eks.cluster_id}"
}

output "kibana_dashboard" {
  value = "https://${aws_opensearch_domain.this.endpoint}/_plugin/kibana"
}

output "kibana_dashboard_username" {
  value = var.opensearch_master_username
}

output "kibana_dashboard_password" {
  value = var.opensearch_master_password
}

output "demo-app_endpoint_url" {
  value = "https://${aws_cloudfront_distribution.e2e_tracing.domain_name}/"
}