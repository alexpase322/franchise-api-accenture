#!/bin/sh
ENDPOINT=http://dynamodb-local:8000

until aws dynamodb list-tables --endpoint-url $ENDPOINT > /dev/null 2>&1; do
  echo "Waiting for DynamoDB Local..."
  sleep 1
done

set -e    # ← NUEVO: si una tabla falla, el script se detiene con error

aws dynamodb create-table --endpoint-url $ENDPOINT \
  --table-name franchises \
  --attribute-definitions AttributeName=id,AttributeType=S \
  --key-schema AttributeName=id,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST

aws dynamodb create-table --endpoint-url $ENDPOINT \
  --table-name branches \
  --attribute-definitions AttributeName=franchiseId,AttributeType=S AttributeName=id,AttributeType=S \
  --key-schema AttributeName=franchiseId,KeyType=HASH AttributeName=id,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST

aws dynamodb create-table --endpoint-url $ENDPOINT \
  --table-name products \
  --attribute-definitions AttributeName=branchId,AttributeType=S AttributeName=id,AttributeType=S \
  --key-schema AttributeName=branchId,KeyType=HASH AttributeName=id,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST

echo "Tables created"