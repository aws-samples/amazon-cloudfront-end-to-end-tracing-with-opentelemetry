# vpc
resource "aws_vpc" "this" {
  cidr_block           = var.cidr
  enable_dns_hostnames = true
  enable_dns_support   = true
  instance_tenancy     = "default"
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

resource "aws_subnet" "backing-private" {
  count             = length(var.azs)
  vpc_id            = aws_vpc.this.id
  cidr_block        = cidrsubnet(cidrsubnet(var.cidr, 4, 2), 2, count.index)
  availability_zone = var.azs[count.index]
  tags = merge(
    { "Name" = "backing-private-${var.azs[count.index]}" }
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
    aws_subnet.eks-private.*.id,
    aws_subnet.backing-private.*.id
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

resource "aws_route_table_association" "backing-private" {
  count = length(var.azs)

  subnet_id      = aws_subnet.backing-private.*.id[count.index]
  route_table_id = aws_route_table.private.*.id[count.index]
}

# backing service subnet group
resource "aws_db_subnet_group" "backing-private" {
  name       = "sbn-group-dev-backing-private"
  subnet_ids = aws_subnet.backing-private.*.id
}
