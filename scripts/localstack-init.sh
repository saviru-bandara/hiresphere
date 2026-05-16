#!/bin/bash
# LocalStack initialization script
# Runs automatically when LocalStack is healthy
# Creates all AWS resources needed for local development

set -e
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1
export AWS_ENDPOINT_URL=http://localhost:4566

echo "=== HireSphere LocalStack Init ==="

# ── DynamoDB Tables ───────────────────────────────────────────────────────────
echo "Creating DynamoDB tables..."

awslocal dynamodb create-table \
  --table-name hiresphere-profiles \
  --billing-mode PAY_PER_REQUEST \
  --attribute-definitions \
    AttributeName=userId,AttributeType=S \
    AttributeName=profileType,AttributeType=S \
  --key-schema \
    AttributeName=userId,KeyType=HASH \
  --global-secondary-indexes '[{
    "IndexName": "profileType-index",
    "KeySchema": [{"AttributeName":"profileType","KeyType":"HASH"}],
    "Projection": {"ProjectionType":"ALL"}
  }]' \
  2>/dev/null || echo "Profiles table already exists"

awslocal dynamodb create-table \
  --table-name hiresphere-messages \
  --billing-mode PAY_PER_REQUEST \
  --attribute-definitions \
    AttributeName=conversationId,AttributeType=S \
    AttributeName=timestamp,AttributeType=S \
  --key-schema \
    AttributeName=conversationId,KeyType=HASH \
    AttributeName=timestamp,KeyType=RANGE \
  2>/dev/null || echo "Messages table already exists"

# ── SQS Queues ────────────────────────────────────────────────────────────────
echo "Creating SQS queues..."

# Dead-letter queues first
awslocal sqs create-queue --queue-name hiresphere-booking-events-dlq        2>/dev/null || true
awslocal sqs create-queue --queue-name hiresphere-notification-dlq           2>/dev/null || true
awslocal sqs create-queue --queue-name hiresphere-github-analysis-dlq        2>/dev/null || true

# Main queues
awslocal sqs create-queue --queue-name hiresphere-booking-events             2>/dev/null || true
awslocal sqs create-queue --queue-name hiresphere-notification-delivery      2>/dev/null || true
awslocal sqs create-queue --queue-name hiresphere-github-analysis            2>/dev/null || true

# ── S3 Buckets ────────────────────────────────────────────────────────────────
echo "Creating S3 buckets..."

awslocal s3 mb s3://hiresphere-submissions-local 2>/dev/null || true

awslocal s3api put-bucket-cors \
  --bucket hiresphere-submissions-local \
  --cors-configuration '{
    "CORSRules": [{
      "AllowedHeaders": ["*"],
      "AllowedMethods": ["GET","PUT","POST"],
      "AllowedOrigins": ["http://localhost:3000"],
      "MaxAgeSeconds": 3600
    }]
  }' 2>/dev/null || true

# ── Cognito User Pool ─────────────────────────────────────────────────────────
echo "Creating Cognito User Pool..."

POOL_ID=$(awslocal cognito-idp create-user-pool \
  --pool-name hiresphere-local \
  --policies '{"PasswordPolicy":{"MinimumLength":8}}' \
  --query 'UserPool.Id' --output text 2>/dev/null || \
  awslocal cognito-idp list-user-pools --max-results 1 --query 'UserPools[0].Id' --output text)

CLIENT_ID=$(awslocal cognito-idp create-user-pool-client \
  --user-pool-id "$POOL_ID" \
  --client-name hiresphere-local-client \
  --no-generate-secret \
  --query 'UserPoolClient.ClientId' --output text 2>/dev/null || echo "existing")

# Create groups
awslocal cognito-idp create-group --group-name CANDIDATE   --user-pool-id "$POOL_ID" 2>/dev/null || true
awslocal cognito-idp create-group --group-name INTERVIEWER --user-pool-id "$POOL_ID" 2>/dev/null || true

# Create test users
awslocal cognito-idp admin-create-user \
  --user-pool-id "$POOL_ID" \
  --username candidate@test.com \
  --temporary-password "Test1234!" \
  --user-attributes Name=email,Value=candidate@test.com \
  2>/dev/null || true

awslocal cognito-idp admin-create-user \
  --user-pool-id "$POOL_ID" \
  --username interviewer@test.com \
  --temporary-password "Test1234!" \
  --user-attributes Name=email,Value=interviewer@test.com \
  2>/dev/null || true

awslocal cognito-idp admin-add-user-to-group \
  --user-pool-id "$POOL_ID" --username interviewer@test.com --group-name INTERVIEWER 2>/dev/null || true

echo ""
echo "=== LocalStack Init Complete ==="
echo "  Cognito Pool ID : $POOL_ID"
echo "  App Client ID   : $CLIENT_ID"
echo "  DynamoDB        : hiresphere-profiles, hiresphere-messages"
echo "  SQS             : 3 queues + 3 DLQs"
echo "  S3              : hiresphere-submissions-local"
