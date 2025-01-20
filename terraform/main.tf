// main.tf

///////////////////////////////////////////////////////////////////////////
// 1) Provider 설정
///////////////////////////////////////////////////////////////////////////

provider "aws" {
  // AWS 리전을 지정합니다. ex) ap-northeast-2(서울)
  region = var.aws_region
}

///////////////////////////////////////////////////////////////////////////
// 2) VPC 및 서브넷 생성
///////////////////////////////////////////////////////////////////////////

// AWS VPC 생성
resource "aws_vpc" "main_vpc" {
  // CIDR: VPC의 IP 범위
  cidr_block = var.vpc_cidr

  tags = {
    Name = "MainVPC"
  }
}

// Public Subnet 생성
resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.main_vpc.id
  cidr_block             = var.public_subnet_cidr
  // 인스턴스에 Public IP가 할당되도록 설정
  map_public_ip_on_launch = true
  availability_zone       = var.public_az

  tags = {
    Name = "PublicSubnet"
  }
}

// Private Subnet 생성
resource "aws_subnet" "private_subnet_a" {
  vpc_id            = aws_vpc.main_vpc.id
  cidr_block        = var.private_subnet_cidr_a
  availability_zone = var.private_az

  tags = {
    Name = "PrivateSubnet"
  }
}

// 1) 추가 Private Subnet (예: ap-northeast-2b) 생성
resource "aws_subnet" "private_subnet_b" {
  vpc_id            = aws_vpc.main_vpc.id
  cidr_block        = var.private_subnet_cidr_b
  availability_zone = "ap-northeast-2b"

  tags = {
    Name = "PrivateSubnetB"
  }
}



///////////////////////////////////////////////////////////////////////////
// 3) 인터넷 게이트웨이 및 라우트 테이블 설정
///////////////////////////////////////////////////////////////////////////

// VPC에 인터넷 게이트웨이 연결
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.main_vpc.id
}

// 라우트 테이블(공인)
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.main_vpc.id

  // 0.0.0.0/0 트래픽은 인터넷 게이트웨이를 통해서 통신
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
}

// 퍼블릭 서브넷에 라우트 테이블 연동
resource "aws_route_table_association" "public_rt_assoc" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_rt.id
}

///////////////////////////////////////////////////////////////////////////
// 4) 보안 그룹 생성
///////////////////////////////////////////////////////////////////////////

// 퍼블릭 서브넷용 보안 그룹 (Nginx)
resource "aws_security_group" "sg_public" {
  name   = "public-sg"
  vpc_id = aws_vpc.main_vpc.id

  // 인바운드 규칙: 80포트(HTTP) 외부 공개
  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  // 아웃바운드 규칙: 모든 트래픽 허용
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

// 프라이빗 서브넷용 보안 그룹 (Spring Boot)
resource "aws_security_group" "sg_private" {
  name   = "private-sg"
  vpc_id = aws_vpc.main_vpc.id

  // 인바운드 규칙: 8080~8081 포트 접근을 Nginx(퍼블릭 서브넷)에서만 허용
  ingress {
    description = "Allow 8080-8081 from Nginx"
    from_port   = 8080
    to_port     = 8081
    protocol    = "tcp"

    // 실제 운영 환경에선 ALB or Nginx 서버의 SG를 참조하거나 CIDR 범위를 명시
    cidr_blocks = [var.public_subnet_cidr]
  }

  // 아웃바운드 규칙: 모든 트래픽 허용
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

///////////////////////////////////////////////////////////////////////////
// 5) EC2 인스턴스 (Nginx, Spring Boot)
///////////////////////////////////////////////////////////////////////////

// 퍼블릭 서브넷에 위치할 Nginx EC2
resource "aws_instance" "ec2_nginx" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  subnet_id              = aws_subnet.public_subnet.id
  vpc_security_group_ids = [aws_security_group.sg_public.id]

  // 퍼블릭 IP 할당
  associate_public_ip_address = true

  tags = {
    Name = "ec2-nginx"
  }
}

// 프라이빗 서브넷에 위치할 Spring Boot EC2 (단일)
resource "aws_instance" "ec2_spring" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  subnet_id              = aws_subnet.private_subnet_a.id
  vpc_security_group_ids = [aws_security_group.sg_private.id]

  tags = {
    Name = "ec2-spring"
  }
}

///////////////////////////////////////////////////////////////////////////
// 6) RDS 생성 (선택사항)
///////////////////////////////////////////////////////////////////////////

resource "aws_db_subnet_group" "my_db_subnet_group" {
  name       = "my-db-subnet-group"
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

  // 민감 정보는 .tfvars나 GitHub Secrets 등을 활용해 관리
  identifier              = var.db_name
  username               = var.db_username
  password               = var.db_password

  publicly_accessible    = false
  skip_final_snapshot    = true
  db_subnet_group_name   = aws_db_subnet_group.my_db_subnet_group.name
  vpc_security_group_ids = [aws_security_group.sg_private.id]

  tags = {
    Name = "my-rds"
  }
}
