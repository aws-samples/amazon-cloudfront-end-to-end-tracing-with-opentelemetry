terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.40.0"
    }
    helm = {
      source  = "hashicorp/helm"
      version = "~> 2.7.1"
    }
  }
  backend "s3" {
    bucket = "<<YOUR S3 BUCKET NAME>>"
    key    = "terraform/terraform.tfstate"
    region = "<<YOUR S3 BUCKET REGION>>"
  }
}

# Configure the AWS Provider
provider "aws" {
  alias  = "seoul"
  region = var.region
}

provider "aws" {
  alias  = "us"
  region = "us-east-1"
}
locals {
  tags = {
    Owner       = "user"
    Environment = "dev"
  }
}
provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
  exec {
    api_version = "client.authentication.k8s.io/v1beta1"
    command     = "aws"
    args        = ["eks", "get-token", "--cluster-name", data.aws_eks_cluster.cluster.id, "--region", var.region]
  }
}

provider "helm" {

  kubernetes {
    host                   = data.aws_eks_cluster.cluster.endpoint
    cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
    exec {
      api_version = "client.authentication.k8s.io/v1beta1"
      command     = "aws"
      args        = ["eks", "get-token", "--cluster-name", data.aws_eks_cluster.cluster.id, "--region", var.region]
    }
  }
}
