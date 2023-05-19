data "aws_caller_identity" "current" {}
data "aws_region" "current" {}
data "aws_eks_cluster" "cluster" {
  name = module.eks.cluster_id
}
data "aws_eks_cluster_auth" "cluster" {
  name = module.eks.cluster_id
}

data "aws_lb" "k8s_ingress_lb" {
  name = "k8s-ingress"
  depends_on = [
    kubernetes_ingress_v1.k8s_ingress
  ]
}

data "aws_ec2_managed_prefix_list" "cloudfront_origin-facing" {
  name = "com.amazonaws.global.cloudfront.origin-facing"
}