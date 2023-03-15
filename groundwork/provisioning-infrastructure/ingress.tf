resource "kubernetes_ingress_v1" "k8s_ingress" {
  depends_on = [helm_release.aws-lb-controller]
  metadata {
    name      = "k8s-ingress"
    namespace = "kube-system"
    annotations = {
      "alb.ingress.kubernetes.io/load-balancer-name" = "k8s-ingress"
      "alb.ingress.kubernetes.io/scheme"             = "internet-facing"
      "alb.ingress.kubernetes.io/healthcheck-port"   = "traffic-port"
      "alb.ingress.kubernetes.io/healthcheck-path"   = "/actuator/health"
      "alb.ingress.kubernetes.io/group.name"         = "k8s-ingress"
      "alb.ingress.kubernetes.io/target-type"        = "ip"
    }
  }

  spec {
    ingress_class_name = "alb"
    rule {
      http {
        path {
          backend {
            service {
              name = "demo-app"
              port {
                number = 8080
              }
            }
          }
          path = "/*"
        }
      }
    }
  }
}

resource "kubernetes_ingress_v1" "k8s_ingress_otel" {
  depends_on = [helm_release.aws-lb-controller]
  metadata {
    name      = "k8s-ingress-otel"
    namespace = "kube-system"
    annotations = {
      "alb.ingress.kubernetes.io/load-balancer-name" = "k8s-ingress"
      "alb.ingress.kubernetes.io/scheme"             = "internet-facing"
      "alb.ingress.kubernetes.io/healthcheck-port"   = local.otel_collector_health_check_port
      "alb.ingress.kubernetes.io/healthcheck-path"   = local.otel_collector_health_check_path
      "alb.ingress.kubernetes.io/group.name"         = "k8s-ingress"
      "alb.ingress.kubernetes.io/target-type"        = "ip"
      "alb.ingress.kubernetes.io/listen-ports"       = "[{\"HTTP\": ${local.otel_collector_otlp_http_port}}]"
    }
  }

  spec {
    ingress_class_name = "alb"
    rule {
      http {
        path {
          backend {
            service {
              name = "opentelemetry-collector"
              port {
                number = local.otel_collector_otlp_http_port
              }
            }
          }
          path = "/*"
        }
      }
    }
  }
}