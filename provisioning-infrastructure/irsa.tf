# EKS OIDC
data "aws_iam_policy_document" "this" {
  for_each = var.irsa_values
  statement {
    actions = ["sts:AssumeRoleWithWebIdentity"]
    effect  = "Allow"

    condition {
      test     = "StringEquals"
      variable = "${replace(module.eks.cluster_oidc_issuer_url, "https://", "")}:sub"
      values = [
        "system:serviceaccount:${each.value.namespace}:sa-${each.value.name}",
      ]
    }

    principals {
      identifiers = [module.eks.oidc_provider_arn]
      type        = "Federated"
    }
  }
}

resource "aws_iam_role" "this" {
  for_each           = var.irsa_values
  name               = join("-", ["role", each.value.name])
  assume_role_policy = data.aws_iam_policy_document.this[each.value.name].json
}

resource "aws_iam_policy" "this" {
  for_each = var.irsa_values
  name     = join("-", ["policy", each.value.name])
  path     = "/"

  policy = each.value.policy
}

resource "aws_iam_role_policy_attachment" "this" {
  for_each   = var.irsa_values
  policy_arn = aws_iam_policy.this[each.value.name].arn
  role       = aws_iam_role.this[each.value.name].name
}

resource "kubernetes_service_account" "this" {
  for_each = var.irsa_values
  metadata {
    name      = "sa-${each.value.name}"
    namespace = each.value.namespace
    annotations = {
      "eks.amazonaws.com/role-arn" = "${aws_iam_role.this[each.value.name].arn}"
    }
  }
}
