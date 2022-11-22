resource "aws_security_group" "allow_msk" {
  name   = "allow_msk"
  vpc_id = aws_vpc.this.id

  ingress = [
    {
      description      = "Allow msk access from VPC"
      cidr_blocks      = []
      description      = ""
      from_port        = 2181
      ipv6_cidr_blocks = []
      prefix_list_ids  = []
      protocol         = "tcp"
      security_groups = [
        module.eks.node_security_group_id
      ]
      self    = false
      to_port = 2181
    },
    {
      description      = "Allow msk access from VPC"
      cidr_blocks      = []
      description      = ""
      from_port        = 9092
      ipv6_cidr_blocks = []
      prefix_list_ids  = []
      protocol         = "tcp"
      security_groups = [
        module.eks.node_security_group_id
      ]
      self    = false
      to_port = 9092
    },
    {
      description      = "Allow msk access from VPC"
      cidr_blocks      = []
      description      = ""
      from_port        = 9094
      ipv6_cidr_blocks = []
      prefix_list_ids  = []
      protocol         = "tcp"
      security_groups = [
        module.eks.node_security_group_id
      ]
      self    = false
      to_port = 9094
    }
  ]

  egress = [
    {
      description      = "Allow cluster egress access to the Internet."
      from_port        = 0
      to_port          = 0
      protocol         = "-1"
      prefix_list_ids  = []
      security_groups  = []
      self             = false
      cidr_blocks      = ["0.0.0.0/0"]
      ipv6_cidr_blocks = ["::/0"]
    }
  ]

  tags = {
    Name = "allow_msk"
  }
}

resource "aws_cloudwatch_log_group" "msk_broker_logs" {
  name = "msk_broker_logs"
}

resource "aws_msk_cluster" "msk-cluster" {
  cluster_name           = "msk-cluster"
  kafka_version          = "2.7.1"
  number_of_broker_nodes = 2

  broker_node_group_info {
    instance_type   = "kafka.m5.large"
    ebs_volume_size = 1000
    client_subnets  = aws_subnet.backing-private.*.id
    security_groups = [aws_security_group.allow_msk.id]
  }

  configuration_info {
    arn      = aws_msk_configuration.initial_config.arn
    revision = aws_msk_configuration.initial_config.latest_revision
  }

  encryption_info {
    encryption_in_transit {
      client_broker = "TLS_PLAINTEXT"
    }
  }
  tags = local.tags
}

resource "aws_msk_configuration" "initial_config" {
  kafka_versions = ["2.7.1"]
  name           = "initial"

  server_properties = <<PROPERTIES
auto.create.topics.enable = true
delete.topic.enable = true
PROPERTIES
}