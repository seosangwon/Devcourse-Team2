// variables.tf

variable "aws_region" {
  type    = string
  default = "ap-northeast-2"
}

variable "vpc_cidr" {
  type    = string
  default = "10.0.0.0/16"
}

variable "public_subnet_cidr" {
  type    = string
  default = "10.0.1.0/24"
}

variable "private_subnet_cidr_a" {
  type    = string
  default = "10.0.2.0/24"
}

variable "private_subnet_cidr_b" {
  type    = string
  default = "10.0.3.0/24"
}

variable "public_az" {
  type    = string
  default = "ap-northeast-2a"
}

variable "private_az" {
  type    = string
  default = "ap-northeast-2a"
}

variable "ami_id" {
  type    = string
  default = "ami-0a998385ed9f45655" // Amazon Linux 2023 (프리티어)
}

variable "instance_type" {
  type    = string
  default = "t2.micro"
}

// RDS 관련 민감 정보
variable "db_name" {
  type        = string
  description = "Database Name"
  sensitive   = true
}

variable "db_username" {
  type        = string
  description = "Database Username"
  sensitive   = true
}

variable "db_password" {
  type        = string
  description = "Database Password"
  sensitive   = true
}
