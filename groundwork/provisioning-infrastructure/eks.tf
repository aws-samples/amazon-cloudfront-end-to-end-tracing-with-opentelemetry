# ---------------------------------------------------------------------------------------------------------------------
# EKS CONTROL PLANE AND MANAGED WORKER NODES DEPLOYED BY THIS MODULE
# ---------------------------------------------------------------------------------------------------------------------
resource "aws_kms_key" "eks" {
  description         = "EKS Secret Encryption Key"
  enable_key_rotation = true
}


module "eks" {
  source          = "terraform-aws-modules/eks/aws"
  version         = "18.31.0"
  cluster_name    = var.eks_cluster_name
  cluster_version = var.kubernetes_version
  subnet_ids      = aws_subnet.eks-private.*.id
  vpc_id          = aws_vpc.this.id

  cluster_endpoint_private_access = true
  cluster_endpoint_public_access  = true

  enable_irsa               = var.enable_irsa
  manage_aws_auth_configmap = false
  cluster_addons = {
    coredns = {
      resolve_conflicts = "OVERWRITE"
    }
    kube-proxy = {}
    vpc-cni = {
      resolve_conflicts = "OVERWRITE"
    }
  }

  iam_role_additional_policies = [
    "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore",
    "arn:aws:iam::aws:policy/AutoScalingFullAccess",
    "arn:aws:iam::aws:policy/CloudWatchFullAccess",
    "arn:aws:iam::aws:policy/ElasticLoadBalancingFullAccess"
  ]

  cluster_encryption_config = [
    {
      provider_key_arn = aws_kms_key.eks.arn
      resources        = ["secrets"]
    }
  ]

  eks_managed_node_group_defaults = {
    ami_type  = "AL2_x86_64"
    disk_size = 50
  }

  eks_managed_node_groups = {
    managed_ng_01 = {
      desired_capacity = 2
      max_capacity     = 5
      min_capacity     = 2
      subnets          = aws_subnet.eks-private.*.id

      instance_types = ["m5.2xlarge"]
      capacity_type  = "ON_DEMAND"
      k8s_labels = {
        type = "managed"
      }
    }

  }

  aws_auth_accounts = [data.aws_caller_identity.current.account_id]
}

resource "aws_security_group_rule" "egress_all" {
  security_group_id = module.eks.node_security_group_id
  type              = "egress"
  to_port           = 0
  protocol          = "-1"
  cidr_blocks       = ["0.0.0.0/0"]
  from_port         = 0
  description       = "egress_all"
}

resource "aws_security_group_rule" "alb_webhook" {
  security_group_id        = module.eks.node_security_group_id
  type                     = "ingress"
  to_port                  = 9443
  protocol                 = "tcp"
  from_port                = 9443
  source_security_group_id = module.eks.cluster_primary_security_group_id
  description              = "Allow access from control plane to webhook port of AWS load balancer controller"
}

module "launch-templates-on-demand" {
  source                   = "./modules/launch-templates"
  cluster_name             = module.eks.cluster_id
  cluster_endpoint         = module.eks.cluster_endpoint
  cluster_ca_base64        = module.eks.cluster_certificate_authority_data
  volume_size              = "50"
  worker_security_group_id = module.eks.cluster_primary_security_group_id
  node_group_name          = var.on_demand_node_group_name
  tags                     = {}
}

resource "helm_release" "aws-lb-controller" {
  name            = "aws-load-balancer-controller"
  repository      = "https://aws.github.io/eks-charts"
  chart           = "aws-load-balancer-controller"
  version         = "1.4.6"
  namespace       = "kube-system"
  cleanup_on_fail = true

  set {
    name  = "clusterName"
    value = var.eks_cluster_name
  }

  set {
    name  = "serviceAccount.create"
    value = "false"
  }

  set {
    name  = "serviceAccount.name"
    value = "sa-${var.irsa_values["aws-lb-controller"]["name"]}"
  }
}