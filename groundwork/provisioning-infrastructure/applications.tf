locals {
  demo_app_port    = 8080
}

resource "helm_release" "demo-app" {
  name  = "demo-app"
  chart = "./charts/demo-app"

  set {
    name  = "image.repository"
    value = var.demo_app_repository
  }
  set {
    name  = "image.tag"
    value = var.demo_app_tag
  }
  set {
    name  = "telemetry.traces.endpoint"
    value = format("http://opentelemetry-collector.default.svc.cluster.local:%d", local.otel_collector_otlp_grpc_port)
  }
}