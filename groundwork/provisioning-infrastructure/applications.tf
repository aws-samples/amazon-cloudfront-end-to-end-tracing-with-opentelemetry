locals {
  demo_app_port = 8080
}

resource "helm_release" "demo-app" {
  name             = "demo-app"
  namespace        = var.k8s_namespace
  chart            = "./charts/demo-app"
  create_namespace = true
  force_update = true

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

  set {
    name  = "namespace"
    value = var.k8s_namespace
  }
}