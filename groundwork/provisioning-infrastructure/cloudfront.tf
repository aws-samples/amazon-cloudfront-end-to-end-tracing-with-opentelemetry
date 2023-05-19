resource "aws_cloudfront_distribution" "e2e_tracing" {
  # checkov:skip=CKV2_AWS_46: This CloudFront Distribution does not have a S3 Origin.
  # checkov:skip=CKV2_AWS_47: This is just a sample for demonstration purposes, so we don't need WAF enabled here.
  # checkov:skip=CKV_AWS_68: This is just a sample for demonstration purposes, so we don't need WAF enabled here.
  # checkov:skip=CKV2_AWS_42: This is just a sample for demonstration purposes, so we don't need a custom SSL certificate here.
  # checkov:skip=CKV_AWS_174: This is just a sample for demonstration purposes, so we don't need a custom SSL certificate here.
  # checkov:skip=CKV_AWS_305: This is just a sample for demonstration purposes, so we don't need a default root object.
  # checkov:skip=CKV_AWS_310: This is just a sample for demonstration purposes, so we don't need origin failover.
  enabled         = true
  is_ipv6_enabled = false
  price_class     = "PriceClass_200"

  origin {
    domain_name = data.aws_lb.k8s_ingress_lb.dns_name
    origin_id   = data.aws_lb.k8s_ingress_lb.dns_name

    custom_origin_config {
      http_port                = 80
      https_port               = 443
      origin_keepalive_timeout = 5
      origin_protocol_policy   = "http-only"
      origin_read_timeout      = 30
      origin_ssl_protocols = [
        "TLSv1.2"
      ]
    }
  }

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }

  default_cache_behavior {
    target_origin_id           = data.aws_lb.k8s_ingress_lb.dns_name
    cached_methods             = ["GET", "HEAD"]
    allowed_methods            = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
    cache_policy_id            = data.aws_cloudfront_cache_policy.cache_disabled.id
    compress                   = true
    viewer_protocol_policy     = "redirect-to-https"
    response_headers_policy_id = aws_cloudfront_response_headers_policy.pass.id
  }

  ordered_cache_behavior {
    function_association {
      event_type   = "viewer-request"
      function_arn = aws_cloudfront_function.cf_tracing_request.arn
    }
    function_association {
      event_type   = "viewer-response"
      function_arn = aws_cloudfront_function.cf_tracing_response.arn
    }

    forwarded_values {
      query_string = false

      cookies {
        forward = "none"
      }
    }

    path_pattern           = "*"
    allowed_methods        = ["GET", "HEAD", "OPTIONS", "PUT", "POST", "PATCH", "DELETE"]
    cached_methods         = ["GET", "HEAD"]
    target_origin_id       = data.aws_lb.k8s_ingress_lb.dns_name
    compress               = true
    viewer_protocol_policy = "redirect-to-https"
  }

  logging_config {
    include_cookies = false
    bucket          = aws_s3_bucket.e2e_tracing.bucket_domain_name
    prefix          = "e2e"
  }
}

resource "aws_s3_bucket" "e2e_tracing" {
  # checkov:skip=CKV_AWS_18: This is just a sample for demonstration purposes, so we don't need access logging.
  # checkov:skip=CKV_AWS_21: This is just a sample for demonstration purposes, so we don't need versioning.
  # checkov:skip=CKV2_AWS_61: This is just a sample for demonstration purposes, so we don't need a lifecycle configuration.
  # checkov:skip=CKV2_AWS_62: This is just a sample for demonstration purposes, so we don't need event notification.
  # checkov:skip=CKV_AWS_144: This is just a sample for demonstration purposes, so we don't need cross-region replication.
  # checkov:skip=CKV_AWS_145: This is just a sample for demonstration purposes, so we don't need KMS encryption.
  bucket = uuid()
  force_destroy = true
}

resource "aws_s3_bucket_public_access_block" "e2e_tracing" {
  bucket = aws_s3_bucket.e2e_tracing.id

  block_public_acls       = true
  block_public_policy     = true
  restrict_public_buckets = true
  ignore_public_acls      = true
}

resource "aws_s3_bucket_ownership_controls" "e2e_tracing" {
  bucket = aws_s3_bucket.e2e_tracing.id
  rule {
    object_ownership = "BucketOwnerPreferred"
  }
}

data "aws_cloudfront_cache_policy" "cache_disabled" {
  name = "Managed-CachingDisabled"
}

resource "aws_cloudfront_function" "cf_tracing_request" {
  name    = "cf-tracing-request"
  runtime = "cloudfront-js-1.0"
  publish = true
  code    = file("${path.module}/cf-tracing-request.js")
}

resource "aws_cloudfront_function" "cf_tracing_response" {
  name    = "cf-tracing-response"
  runtime = "cloudfront-js-1.0"
  publish = true
  code    = file("${path.module}/cf-tracing-response.js")
}

resource "aws_cloudwatch_log_group" "cf_tracing_response" {
  # checkov:skip=CKV_AWS_158: This is just a sample for demonstration purposes, so we don't need KMS key here.
  name              = "/aws/cloudfront/function/cf-tracing-response"
  provider          = aws.us
  retention_in_days = 7
}

resource "aws_iam_role" "cf_tracing_processor" {
  name = "cf_tracing_processor"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
  managed_policy_arns = ["arn:aws:iam::aws:policy/service-role/AWSLambdaVPCAccessExecutionRole"]
}

data "archive_file" "cf_tracing_processor" {
  type             = "zip"
  source_file      = "${path.module}/cf-tracing-processor.js"
  output_file_mode = "0666"
  output_path      = "${path.module}/cf_tracing_processor.zip"
}

resource "aws_lambda_function" "cf_tracing_processor" {
  # checkov:skip=CKV_AWS_272: This is just a sample for demonstration purposes, so we don't need to validate code-signing here.
  # checkov:skip=CKV_AWS_173: This is just a sample for demonstration purposes, so we don't need a KMS encryption here.
  # checkov:skip=CKV_AWS_116: This is just a sample for demonstration purposes, so we don't need a deadletter queue here.
  function_name = "cf_tracing_processor"
  role          = aws_iam_role.cf_tracing_processor.arn

  runtime  = "nodejs16.x"
  handler  = "cf-tracing-processor.handler"
  filename = "${path.module}/cf_tracing_processor.zip"

  provider = aws.seoul

  environment {
    variables = {
      OPENTELEMETRY_COLLECTOR_HOSTNAME       = data.aws_lb.k8s_ingress_lb.dns_name
      OPENTELEMETRY_COLLECTOR_OTLP_HTTP_PORT = local.otel_collector_otlp_http_port
      TRACE_SAMPLING_RATE                    = var.trace_sampling_rate
      CLOUDFRONT_DOMAIN_NAME                 = aws_cloudfront_distribution.e2e_tracing.domain_name
    }
  }
  tracing_config {
    mode = "Active"
  }
  vpc_config {
    security_group_ids = [aws_security_group.cf_tracing_processor.id]
    subnet_ids         = aws_subnet.eks-private.*.id
  }
  reserved_concurrent_executions = 100
}

resource "aws_security_group" "cf_tracing_processor" {
  name        = "cf_tracing_processor"
  description = "Allow HTTP outbound traffic to otel-collector"
  vpc_id      = aws_vpc.this.id

  egress {
    description = "to otel-collector endpoint"
    from_port        = local.otel_collector_otlp_http_port
    to_port          = local.otel_collector_otlp_http_port
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
}

resource "aws_cloudwatch_log_group" "cf_tracing_processor" {
  # checkov:skip=CKV_AWS_158: This is just a sample for demonstration purposes, so we don't need a KMS encryption here.
  name              = "/aws/lambda/cf_tracing_processor"
  provider          = aws.us
  retention_in_days = 7
}

resource "aws_cloudwatch_log_subscription_filter" "cf_tracing_response_filter" {
  name            = "cf_tracing_response_filter"
  log_group_name  = aws_cloudwatch_log_group.cf_tracing_response.name
  filter_pattern  = "[request_id, start_time = 1*, end_time = 1*, b3]"
  destination_arn = aws_lambda_function.cf_tracing_processor.arn

  provider = aws.us

  depends_on = [aws_lambda_permission.allow_cloudwatch]
}

resource "aws_lambda_permission" "allow_cloudwatch" {
  statement_id  = "AllowExecutionFromCloudWatch"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.cf_tracing_processor.function_name
  principal     = "logs.us-east-1.amazonaws.com"
  source_arn    = "${aws_cloudwatch_log_group.cf_tracing_response.arn}:*"

  provider = aws.seoul
}


resource "aws_cloudfront_response_headers_policy" "pass" {
  provider = aws.us
  name     = "pass"
  security_headers_config {
    strict_transport_security {
      access_control_max_age_sec = 31536000
      include_subdomains         = true
      override                   = true
      preload                    = true
    }
  }
}
