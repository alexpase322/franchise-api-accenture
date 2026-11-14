output "table_names" {
  description = "Names of the DynamoDB tables"
  value = [
    aws_dynamodb_table.franchises.name,
    aws_dynamodb_table.branches.name,
    aws_dynamodb_table.products.name,
  ]
}