# vpc
resource "aws_vpc" "this" {
  # checkov:skip=CKV2_AWS_11: This is just a sample for demonstration purposes, so we don't need to configure VPC flow log here.
  cidr_block           = var.cidr
  enable_dns_hostnames = true
  enable_dns_support   = true
  instance_tenancy     = "default"
}
resource "aws_default_security_group" "default" {
  vpc_id = aws_vpc.this.id
}

# alb security group
resource "aws_security_group" "alb_sg" {
  # checkov:skip=CKV2_AWS_5: This security group is attached to the ALB defined in ingress.tf
  name        = "allow_http"
  description = "Allow HTTP inbound traffic"
  vpc_id      = aws_vpc.this.id

  ingress {
    description      = "HTTP from Public"
    from_port        = 80
    to_port          = 80
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  ingress {
    description      = "Otel Collector from Public"
    from_port        = local.otel_collector_otlp_http_port
    to_port          = local.otel_collector_otlp_http_port
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  egress {
    description      = "allow all"
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  tags = {
    Name = "allow_http"
  }
}

# subnets
resource "aws_subnet" "public" {
  count             = length(var.azs)
  vpc_id            = aws_vpc.this.id
  cidr_block        = cidrsubnet(cidrsubnet(var.cidr, 4, 0), 2, count.index)
  availability_zone = var.azs[count.index]
  tags = merge(
    tomap({ "Name" = "public-${var.azs[count.index]}" }),
    tomap({ "kubernetes.io/role/elb" = "1" }),
    tomap({ "kubernetes.io/cluster/${var.eks_cluster_name}" = "shared" })
  )
}

resource "aws_subnet" "eks-private" {
  count             = length(var.azs)
  vpc_id            = aws_vpc.this.id
  cidr_block        = cidrsubnet(cidrsubnet(var.cidr, 4, 1), 2, count.index)
  availability_zone = var.azs[count.index]
  tags = merge(
    tomap({ "Name" = "eks-private-${var.azs[count.index]}" }),
    tomap({ "kubernetes.io/role/internal-elb" = "1" }),
    tomap({ "kubernetes.io/cluster/${var.eks_cluster_name}" = "shared" })
  )
}

# default network ACL
resource "aws_default_network_acl" "dev_default" {
  default_network_acl_id = aws_vpc.this.default_network_acl_id

  ingress {
    protocol   = -1
    rule_no    = 100
    action     = "allow"
    cidr_block = "0.0.0.0/0"
    from_port  = 0
    to_port    = 0
  }

  egress {
    protocol   = -1
    rule_no    = 100
    action     = "allow"
    cidr_block = "0.0.0.0/0"
    from_port  = 0
    to_port    = 0
  }

  subnet_ids = flatten([
    aws_subnet.public.*.id,
    aws_subnet.eks-private.*.id
  ])
}

resource "aws_eip" "nat" {
  count = length(var.azs)

  vpc = true
}

resource "aws_nat_gateway" "this" {
  count = length(var.azs)

  allocation_id = aws_eip.nat.*.id[count.index]
  subnet_id     = aws_subnet.public.*.id[count.index]
}

resource "aws_internet_gateway" "this" {
  vpc_id = aws_vpc.this.id
}

# public route table
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.this.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.this.id
  }
}

# private route table
resource "aws_route_table" "private" {
  count = length(var.azs)

  vpc_id = aws_vpc.this.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.this.*.id[count.index]
  }
}

# public route table association
resource "aws_route_table_association" "public" {
  count = length(var.azs)

  subnet_id      = aws_subnet.public.*.id[count.index]
  route_table_id = aws_route_table.public.id
}

# private route table association
resource "aws_route_table_association" "eks-private" {
  count = length(var.azs)

  subnet_id      = aws_subnet.eks-private.*.id[count.index]
  route_table_id = aws_route_table.private.*.id[count.index]
}
