resource "aws_elasticache_subnet_group" "elasticache" {
  name       = "eleaticache-subnet-group"
  subnet_ids = aws_subnet.backing-private.*.id
}

resource "aws_elasticache_replication_group" "elasticache" {
  replication_group_id = "elasticache-cluster"
  description          = "Elasticache cluster for Demo"
  engine               = "redis"
  engine_version       = "6.x"
  node_type            = "cache.m5.large"
  port                 = 6379
  parameter_group_name = "default.redis6.x.cluster.on"

  snapshot_retention_limit   = 5
  subnet_group_name          = aws_elasticache_subnet_group.elasticache.name
  automatic_failover_enabled = true

  replicas_per_node_group = 1
  num_node_groups         = 1
  # cluster_mode {
  #   replicas_per_node_group = 1
  #   num_node_groups         = 3
  # }

  security_group_ids = [aws_security_group.elasticache-cluster-sg.id]
}

# elasticache security group
resource "aws_security_group" "elasticache-cluster-sg" {
  name        = "elasticache-cluster-sg"
  description = "elasticache-cluster-sg"
  vpc_id      = aws_vpc.this.id
  ingress = [
    {
      cidr_blocks      = []
      description      = ""
      from_port        = 6379
      ipv6_cidr_blocks = []
      prefix_list_ids  = []
      protocol         = "tcp"
      security_groups = [
        module.eks.node_security_group_id
      ]
      self    = false
      to_port = 6379
    },
  ]
  egress = []
  tags   = {}
}