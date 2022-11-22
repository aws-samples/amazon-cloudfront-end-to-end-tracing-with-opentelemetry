# ---------------------------------------------------------------------------------------------------------------------
# Random Password for RDS
# ---------------------------------------------------------------------------------------------------------------------
resource "random_password" "master" {
  length = 10
}
# ---------------------------------------------------------------------------------------------------------------------
# RDS Aurora PostgreSQL
# ---------------------------------------------------------------------------------------------------------------------
module "rds" {
  source  = "terraform-aws-modules/rds-aurora/aws"
  version = "7.1.0"

  database_name  = "postgres"
  name           = "aurora-db-postgres"
  engine         = "aurora-postgresql"
  engine_version = "11.13"
  instance_class = "db.r6g.large"
  instances = {
    one = {}
  }

  vpc_id  = aws_vpc.this.id
  subnets = aws_subnet.backing-private.*.id

  allowed_security_groups = [module.eks.node_security_group_id]

  storage_encrypted   = true
  apply_immediately   = true
  monitoring_interval = 10

  create_random_password = false
  master_username        = "postgres"
  master_password        = "postgres1234"
  #  master_password        = random_password.master.result

  # db_parameter_group_name         = "default"
  # db_cluster_parameter_group_name = "default"

  performance_insights_enabled    = true
  enabled_cloudwatch_logs_exports = ["postgresql"]

  tags = local.tags
}

resource "aws_security_group" "allow_postgres" {
  name        = "allow_postgres"
  description = "Allow PostgreSQL inbound traffic"
  vpc_id      = aws_vpc.this.id

  ingress = [
    {
      description      = "Allow PostgreSQL access from VPC"
      from_port        = 5432
      to_port          = 5432
      protocol         = "tcp"
      prefix_list_ids  = []
      security_groups  = []
      self             = false
      cidr_blocks      = [aws_vpc.this.cidr_block]
      ipv6_cidr_blocks = []
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
    Name = "allow_postgres"
  }
}
