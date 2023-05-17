resource "aws_opensearch_domain" "this" {
  # checkov:skip=CKV_AWS_137: This is just a sample for demonstration purposes, so we will use public domain here.
  # checkov:skip=CKV_AWS_248: This is just a sample for demonstration purposes, so we will use public domain here.
  # checkov:skip=CKV_AWS_247: This is just a sample for demonstration purposes, so we don't need to encrypt all data with CMK here.
  # checkov:skip=CKV2_AWS_59: This is just a sample for demonstration purposes, so we don't need a dedicated master node here.
  # checkov:skip=CKV_AWS_317: This is just a sample for demonstration purposes, so we don't need audit logging.
  # checkov:skip=CKV_AWS_318: This is just a sample for demonstration purposes, so we don't need HA.
  domain_name    = "opensearch"
  engine_version = "Elasticsearch_7.10"

  cluster_config {
    instance_type          = var.opensearch_instance_type
    instance_count         = 2
    zone_awareness_enabled = true
  }

  ebs_options {
    ebs_enabled = true
    volume_size = 1024
    volume_type = "gp2"
  }

  advanced_security_options {
    enabled                        = true
    internal_user_database_enabled = true
    master_user_options {
      master_user_name     = var.opensearch_master_username
      master_user_password = var.opensearch_master_password
    }
  }

  node_to_node_encryption {
    enabled = true
  }

  encrypt_at_rest {
    enabled = true
  }

  domain_endpoint_options {
    enforce_https       = true
    tls_security_policy = "Policy-Min-TLS-1-2-2019-07"
  }
  log_publishing_options {
    cloudwatch_log_group_arn = aws_cloudwatch_log_group.opensearch.arn
    log_type                 = "INDEX_SLOW_LOGS"
  }

  access_policies = <<POLICY
  {
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": "*"
      },
      "Action": "es:*",
      "Resource": "arn:aws:es:${var.region}:${data.aws_caller_identity.current.account_id}:domain/${var.opensearch_domain}/*"
    }
  ]
}
POLICY

}

resource "aws_cloudwatch_log_group" "opensearch" {
  # checkov:skip=CKV_AWS_158: This is just a sample for demonstration purposes, so we don't need KMS key here.
  name              = "opensearch"
  retention_in_days = 7
}

data "aws_iam_policy_document" "opensearch_log_group" {
  statement {
    effect = "Allow"

    principals {
      type        = "Service"
      identifiers = ["es.amazonaws.com"]
    }

    actions = [
      "logs:PutLogEvents",
      "logs:PutLogEventsBatch",
      "logs:CreateLogStream",
    ]

    resources = ["arn:aws:logs:*"]
  }
}

resource "aws_cloudwatch_log_resource_policy" "opensearch_log_group" {
  policy_name     = "opensearch_log_group"
  policy_document = data.aws_iam_policy_document.opensearch_log_group.json
}

