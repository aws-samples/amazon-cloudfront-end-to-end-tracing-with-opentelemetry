resource "aws_opensearch_domain" "this" {
  domain_name    = "opensearch"
  engine_version = "Elasticsearch_7.10"

  cluster_config {
    instance_type = var.opensearch_instance_type
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