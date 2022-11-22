locals {
  data_prepper_endpoint            = "data-prepper-headless.default.svc.cluster.local:21890"
  otel_collector_otlp_grpc_port    = 4317
  otel_collector_otlp_http_port    = 4318
  otel_collector_health_check_port = 13133
  otel_collector_health_check_path = "/"
}

###########################
## DATA PREPPER
###########################

resource "helm_release" "data-prepper" {
  chart      = "./charts/data-prepper"
  name       = "data-prepper"
  depends_on = [aws_opensearch_domain.this]

  set {
    name  = "opensearch.host"
    value = format("https://%s", aws_opensearch_domain.this.endpoint)
  }
  set {
    name  = "opensearch.username"
    value = var.opensearch_master_username
  }
  set {
    name  = "opensearch.password"
    value = var.opensearch_master_password
  }
}

###########################
## OPENTELEMETRY COLLECTOR
###########################

resource "helm_release" "opentelemetry-collector" {
  name            = "opentelemetry-collector"
  repository      = "https://open-telemetry.github.io/opentelemetry-helm-charts"
  chart           = "opentelemetry-collector"
  version         = "0.37.0"
  namespace       = "default"
  cleanup_on_fail = true
  depends_on      = [helm_release.data-prepper]

  values = [templatefile("./templates/opentelemetry-collector-values.tftpl", {
    DATA_PREPPER_ENDPOINT = local.data_prepper_endpoint,
    OTLP_GRPC_PORT        = local.otel_collector_otlp_grpc_port,
    OTLP_HTTP_PORT        = local.otel_collector_otlp_http_port,
    HEALTH_CHECK_PORT     = local.otel_collector_health_check_port,
    HEALTH_CHECK_PATH     = local.otel_collector_health_check_path
  })]
}