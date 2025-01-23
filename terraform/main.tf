//////////////////////////////////////////////////////////////////////////
// 1) Provider 설정
///////////////////////////////////////////////////////////////////////////
provider "aws" {
  region = var.aws_region
}


///////////////////////////////////////////////////////////////////////////
// 2) IAM Role + Instance Profile (SSM 접근 권한)
///////////////////////////////////////////////////////////////////////////
data "aws_iam_policy_document" "ssm_trust" {
  statement {
    actions = ["sts:AssumeRole"]
    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "ssm_role" {
  name               = "ec2-ssm-role"
  assume_role_policy = data.aws_iam_policy_document.ssm_trust.json
}

# EC2가 SSM에 접근할 수 있도록 AmazonSSMManagedInstanceCore 정책 부여
resource "aws_iam_role_policy_attachment" "attach_ssm_core" {
  role       = aws_iam_role.ssm_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_instance_profile" "ssm_instance_profile" {
  name = "ec2-ssm-instance-profile"
  role = aws_iam_role.ssm_role.name
}


///////////////////////////////////////////////////////////////////////////
// 3) VPC & 서브넷 생성
///////////////////////////////////////////////////////////////////////////
resource "aws_vpc" "main_vpc" {
  cidr_block = var.vpc_cidr
  tags = {
    Name = "MainVPC"
  }
}

// Public Subnet
resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.main_vpc.id
  cidr_block             = var.public_subnet_cidr
  availability_zone       = var.public_az
  map_public_ip_on_launch = true

  tags = {
    Name = "PublicSubnet"
  }
}

// Private Subnet A
resource "aws_subnet" "private_subnet_a" {
  vpc_id            = aws_vpc.main_vpc.id
  cidr_block        = var.private_subnet_cidr_a
  availability_zone = var.private_az_a

  tags = {
    Name = "PrivateSubnetA"
  }
}

// Private Subnet B
resource "aws_subnet" "private_subnet_b" {
  vpc_id            = aws_vpc.main_vpc.id
  cidr_block        = var.private_subnet_cidr_b
  availability_zone = var.private_az_b

  tags = {
    Name = "PrivateSubnetB"
  }
}


///////////////////////////////////////////////////////////////////////////
// 4) 인터넷 게이트웨이 & 라우팅 (Public 서브넷)
///////////////////////////////////////////////////////////////////////////
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.main_vpc.id
}

resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.main_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "PublicRouteTable"
  }
}

// Public Subnet에 라우트 테이블 연결
resource "aws_route_table_association" "public_assoc" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_rt.id
}


///////////////////////////////////////////////////////////////////////////
// 5) Private Subnet용 Route Table
///////////////////////////////////////////////////////////////////////////
resource "aws_route_table" "private_rt" {
  vpc_id = aws_vpc.main_vpc.id
  tags = {
    Name = "PrivateRouteTable"
  }
}

resource "aws_route_table_association" "private_assoc_a" {
  subnet_id      = aws_subnet.private_subnet_a.id
  route_table_id = aws_route_table.private_rt.id
}

resource "aws_route_table_association" "private_assoc_b" {
  subnet_id      = aws_subnet.private_subnet_b.id
  route_table_id = aws_route_table.private_rt.id
}





///////////////////////////////////////////////////////////////////////////
// 7) 보안 그룹
///////////////////////////////////////////////////////////////////////////
resource "aws_security_group" "sg_public" {
  name   = "public-sg"
  vpc_id = aws_vpc.main_vpc.id

  // 80포트 오픈 (HTTP)
  ingress {
    description = "Allow HTTP (80)"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  // Spring Boot 등 8080 포트도 오픈
  ingress {
    description = "Allow Spring Boot (8080)"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }


  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "sg-public"
  }
}


# RDS용 Private Subnet SG
resource "aws_security_group" "sg_private" {
  name   = "private-sg"
  vpc_id = aws_vpc.main_vpc.id

  # RDS는 3306 포트를 열 수도 있음 (예: MySQL)
  ingress {
    description = "Allow MySQL (3306) from ???"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    # cidr_blocks = [ ... ]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "sg-private"
  }
}



///////////////////////////////////////////////////////////////////////////
// 8) EC2 인스턴스 (Nginx + Spring Boot 둘 다 올릴 예정)
///////////////////////////////////////////////////////////////////////////
resource "aws_instance" "ec2_nginx" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  subnet_id              = aws_subnet.public_subnet.id
  vpc_security_group_ids = [aws_security_group.sg_public.id]
  associate_public_ip_address = true

  # SSM Role 연결 (옵션)
  iam_instance_profile   = aws_iam_instance_profile.ssm_instance_profile.name

  tags = {
    Name = "ec2-nginx-spring"
  }
}


///////////////////////////////////////////////////////////////////////////
// 9) SSM Document & Association (Install Docker)
///////////////////////////////////////////////////////////////////////////
resource "aws_ssm_document" "install_docker_doc" {
  name          = "Install-Docker"
  document_type = "Command"

  content = <<EOF
{
  "schemaVersion": "2.2",
  "description": "Install Docker on EC2 (Linux)",
  "mainSteps": [
    {
      "action": "aws:runShellScript",
      "name": "installDocker",
      "inputs": {
        "runCommand": [
          "sudo apt-get update -y || sudo yum update -y",
          "sudo apt-get install -y docker.io || sudo yum install -y docker",
          "sudo systemctl enable docker",
          "sudo systemctl start docker",
          "sudo usermod -aG docker \$USER",
          "echo 'Docker installation completed.'"
        ]
      }
    }
  ]
}
EOF
}

# 하나의 EC2에서 Docker 설치(필요 시 Nginx, Spring Boot 컨테이너 동시 실행 가능)
resource "aws_ssm_association" "install_docker_on_nginx" {
  name        = aws_ssm_document.install_docker_doc.name
  instance_id = aws_instance.ec2_nginx.id
}


///////////////////////////////////////////////////////////////////////////
// 10) RDS 예시 (선택)
///////////////////////////////////////////////////////////////////////////
resource "aws_db_subnet_group" "my_db_subnet_group" {
  name = "my-db-subnet-group-new"
  subnet_ids = [
    aws_subnet.private_subnet_a.id,
    aws_subnet.private_subnet_b.id
  ]
  tags = {
    Name = "my-db-subnet-group"
  }
}

resource "aws_db_instance" "my_rds" {
  allocated_storage      = 20
  engine                 = "mysql"
  engine_version         = "8.0"
  instance_class         = "db.t4g.micro"
  identifier             = var.db_name
  username               = var.db_username
  password               = var.db_password

  publicly_accessible    = false
  skip_final_snapshot    = true
  db_subnet_group_name   = aws_db_subnet_group.my_db_subnet_group.name

  # private-subnet용 보안 그룹
  vpc_security_group_ids = [
    # aws_security_group.sg_private.id
  ]

  tags = {
    Name = "my-rds"
  }
}
