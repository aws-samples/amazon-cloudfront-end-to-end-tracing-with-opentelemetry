variable "region" {
  type    = string
  default = "ap-northeast-2"
}

variable "cidr" {
  type    = string
  default = "10.0.0.0/16"
}

variable "azs" {
  default = ["ap-northeast-2a", "ap-northeast-2c"]
}

variable "eks_cluster_name" {
  type    = string
  default = "eks-sample"
}

variable "on_demand_node_group_name" {
  type        = string
  default     = "mg-m5-on-demand"
  description = "AWS eks managed node group name"
}

variable "kubernetes_version" {
  type        = string
  default     = "1.23"
  description = "Desired Kubernetes master version. If you do not specify a value, the latest available version is used"
}

variable "enable_irsa" {
  type        = bool
  default     = true
  description = "Indicates whether or not the Amazon EKS public API server endpoint is enabled. Default to AWS EKS resource and it is true"
}

variable "irsa_values" {

}

variable "opensearch_domain" {
  default = "opensearch"
}

variable "opensearch_instance_type" {
  default = "r5.large.search"
}

variable "opensearch_master_username" {
  default = "master"
}

variable "opensearch_master_password" {
  default = "Master!234"
}

variable "trace_sampling_rate" {
  type        = number
  default     = 100
  description = "Trace sampling rate between 0 and 100 (default: always)"
}

variable "demo_app_repository" {}
variable "demo_app_tag" {
  default = "latest"
}

variable "k8s_namespace" {
  default = "e2e-blog-sample"
}