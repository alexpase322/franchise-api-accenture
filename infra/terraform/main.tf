resource "aws_dynamodb_table" "franchises" {
  name         = "franchises"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "id"

  attribute {
    name = "id"
    type = "S"
  }
}

resource "aws_dynamodb_table" "branches" {
  name         = "branches"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "franchiseId"
  range_key    = "id"

  attribute {
    name = "franchiseId"
    type = "S"
  }

  attribute {
    name = "id"
    type = "S"
  }
}

resource "aws_dynamodb_table" "products" {
  name         = "products"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "branchId"
  range_key    = "id"

  attribute {
    name = "branchId"
    type = "S"
  }

  attribute {
    name = "id"
    type = "S"
  }
}