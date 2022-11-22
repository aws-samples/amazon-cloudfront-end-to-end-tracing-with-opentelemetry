locals {
  delivery_service_port = 8081
  order_service_port    = 8080
}

resource "helm_release" "e2e-sample-delivery" {
  name  = "e2e-sample-delivery"
  chart = "${path.root}/../install-sample-microservices/e2e-sample-delivery"

  set {
    name  = "image.repository"
    value = var.delivery_image_repository
  }
  set {
    name  = "image.tag"
    value = var.delivery_image_tag
  }
  set {
    name  = "application.kafkaConnectionString"
    value = replace(aws_msk_cluster.msk-cluster.bootstrap_brokers, ",", "\\,")
    type  = "string"
  }
  set {
    name  = "application.dbHost"
    value = module.rds.cluster_endpoint
  }
  set {
    name  = "application.dbPort"
    value = module.rds.cluster_port
  }
  set {
    name  = "application.dbName"
    value = module.rds.cluster_database_name
  }
  set {
    name  = "application.dbUsername"
    value = module.rds.cluster_master_username
  }
  set {
    name  = "application.dbPassword"
    value = module.rds.cluster_master_password
  }
  set {
    name  = "telemetry.traces.endpoint"
    value = format("http://opentelemetry-collector.default.svc.cluster.local:%d", local.otel_collector_otlp_grpc_port)
  }
}

resource "helm_release" "e2e-sample-order" {
  name  = "e2e-sample-order"
  chart = "${path.root}/../install-sample-microservices/e2e-sample-order"

  set {
    name  = "image.repository"
    value = var.order_image_repository
  }
  set {
    name  = "image.tag"
    value = var.order_image_tag
  }
  set {
    name  = "application.deliveryServiceUrl"
    value = "http://e2e-sample-delivery.default.svc.cluster.local:8081"
  }
  set {
    name  = "application.kafkaConnectionString"
    value = replace(aws_msk_cluster.msk-cluster.bootstrap_brokers, ",", "\\,")
    type  = "string"
  }
  set {
    name  = "application.dbHost"
    value = module.rds.cluster_endpoint
  }
  set {
    name  = "application.dbPort"
    value = module.rds.cluster_port
  }
  set {
    name  = "application.dbName"
    value = module.rds.cluster_database_name
  }
  set {
    name  = "application.dbUsername"
    value = module.rds.cluster_master_username
  }
  set {
    name  = "application.dbPassword"
    value = module.rds.cluster_master_password
  }
  set {
    name  = "application.redisHost"
    value = aws_elasticache_replication_group.elasticache.configuration_endpoint_address
  }
  set {
    name  = "application.redisPort"
    value = aws_elasticache_replication_group.elasticache.port
  }
  set {
    name  = "telemetry.traces.endpoint"
    value = format("http://opentelemetry-collector.default.svc.cluster.local:%d", local.otel_collector_otlp_grpc_port)
  }
}
