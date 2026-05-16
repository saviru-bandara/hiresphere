#!/bin/bash
# ─────────────────────────────────────────────────────────────────────────────
# HireSphere — Full AWS Deployment Script
#
# Phases:
#   1. Deploy CloudFormation stack
#   2. Fetch stack outputs and populate K8s configs
#   3. Authenticate kubectl
#   4. Install AWS Load Balancer Controller
#   5. Apply Kubernetes manifests
#   6. Build + push Docker images to ECR
#   7. Restart deployments to pull latest images
#   8. Deploy Lambda functions
#   9. Print Viva demo endpoints
#
# Usage: DB_PASSWORD=<password> ./scripts/deploy.sh
# ─────────────────────────────────────────────────────────────────────────────
set -euo pipefail

REGION=${AWS_REGION:-us-east-1}
STACK_NAME=${STACK_NAME:-hiresphere-viva}
CLUSTER_NAME=hiresphere-cluster
NAMESPACE=hiresphere
DB_PASSWORD=${DB_PASSWORD:?"DB_PASSWORD env var is required"}
STRIPE_KEY=${STRIPE_SECRET_KEY:-sk_test_placeholder}

echo "========================================"
echo " HireSphere Full Deployment"
echo " Stack   : $STACK_NAME"
echo " Region  : $REGION"
echo "========================================"

# ── Phase 1: CloudFormation ───────────────────────────────────────────────────
echo ""
echo ">>> Phase 1: Deploying CloudFormation stack..."
aws cloudformation deploy \
  --template-file infrastructure/hiresphere-infra.yaml \
  --stack-name "$STACK_NAME" \
  --region "$REGION" \
  --capabilities CAPABILITY_NAMED_IAM \
  --parameter-overrides \
    DBPassword="$DB_PASSWORD" \
    EnvironmentName=hiresphere
echo "CloudFormation stack deployed."

# ── Fetch Stack Outputs ───────────────────────────────────────────────────────
echo ""
echo ">>> Fetching CloudFormation outputs..."

get_output() {
  aws cloudformation describe-stacks \
    --stack-name "$STACK_NAME" \
    --region "$REGION" \
    --query "Stacks[0].Outputs[?OutputKey=='$1'].OutputValue" \
    --output text
}

RDS_ENDPOINT=$(get_output RDSEndpoint)
COGNITO_POOL_ID=$(get_output CognitoUserPoolId)
COGNITO_CLIENT_ID=$(get_output CognitoClientId)
S3_BUCKET=$(get_output S3BucketName)
BOOKING_QUEUE=$(get_output BookingEventsQueueURL)
GITHUB_QUEUE=$(get_output GitHubAnalysisQueueURL)
COTURN_IP=$(get_output CoturnPublicIP)
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
ECR_BASE="${ACCOUNT_ID}.dkr.ecr.${REGION}.amazonaws.com/hiresphere"

echo "  RDS Endpoint     : $RDS_ENDPOINT"
echo "  Cognito Pool     : $COGNITO_POOL_ID"
echo "  S3 Bucket        : $S3_BUCKET"
echo "  Coturn IP        : $COTURN_IP"

# ── Phase 2: Patch K8s ConfigMaps with real values ────────────────────────────
echo ""
echo ">>> Phase 2: Patching Kubernetes ConfigMaps..."
sed -i "s|REPLACE_WITH_CFN_OUTPUT|$COGNITO_POOL_ID|g"           k8s/configmaps/configmaps.yaml
sed -i "s|REPLACE_WITH_CFN_OUTPUT_BookingEventsQueueURL|$BOOKING_QUEUE|g" k8s/configmaps/configmaps.yaml
sed -i "s|REPLACE_WITH_CFN_OUTPUT_S3BucketName|$S3_BUCKET|g"    k8s/configmaps/configmaps.yaml
sed -i "s|REPLACE_WITH_CFN_OUTPUT_GitHubAnalysisQueueURL|$GITHUB_QUEUE|g" k8s/configmaps/configmaps.yaml
sed -i "s|REPLACE_WITH_COTURN_IP|$COTURN_IP|g"                  k8s/configmaps/configmaps.yaml

# Patch deployments with real ECR image URLs and account ID
sed -i "s|ACCOUNT_ID|$ACCOUNT_ID|g; s|REGION|$REGION|g" k8s/deployments/deployments.yaml
sed -i "s|ACCOUNT_ID|$ACCOUNT_ID|g"                      k8s/secrets/service-accounts.yaml

# ── Phase 3: Authenticate kubectl ─────────────────────────────────────────────
echo ""
echo ">>> Phase 3: Configuring kubectl..."
aws eks update-kubeconfig \
  --name "$CLUSTER_NAME" \
  --region "$REGION"
kubectl cluster-info

# ── Phase 4: AWS Load Balancer Controller ─────────────────────────────────────
echo ""
echo ">>> Phase 4: Installing AWS Load Balancer Controller..."
helm repo add eks https://aws.github.io/eks-charts 2>/dev/null || true
helm repo update
helm upgrade --install aws-load-balancer-controller eks/aws-load-balancer-controller \
  -n kube-system \
  --set clusterName="$CLUSTER_NAME" \
  --set serviceAccount.create=true \
  --set serviceAccount.name=aws-load-balancer-controller \
  --wait

# ── Phase 5: Apply Kubernetes manifests ───────────────────────────────────────
echo ""
echo ">>> Phase 5: Applying Kubernetes manifests..."
kubectl apply -f k8s/namespace/
kubectl apply -f k8s/secrets/service-accounts.yaml

# Create secrets from AWS Secrets Manager
DB_CREDS=$(aws secretsmanager get-secret-value \
  --secret-id hiresphere/rds-credentials \
  --query SecretString --output text)
DB_USER=$(echo "$DB_CREDS" | python3 -c "import sys,json; print(json.load(sys.stdin)['username'])")

kubectl create secret generic hiresphere-secrets \
  --namespace "$NAMESPACE" \
  --from-literal=DB_HOST="$RDS_ENDPOINT" \
  --from-literal=DB_USER="$DB_USER" \
  --from-literal=DB_PASSWORD="$DB_PASSWORD" \
  --from-literal=STRIPE_SECRET_KEY="$STRIPE_KEY" \
  --dry-run=client -o yaml | kubectl apply -f -

kubectl apply -f k8s/configmaps/
kubectl apply -f k8s/deployments/
kubectl apply -f k8s/services/
kubectl apply -f k8s/hpa/
kubectl apply -f k8s/ingress/

# ── Phase 6: Build & Push Docker images ───────────────────────────────────────
echo ""
echo ">>> Phase 6: Building and pushing Docker images..."
./scripts/build-and-push.sh

# ── Phase 7: Rolling restart to pull latest images ────────────────────────────
echo ""
echo ">>> Phase 7: Rolling restart of all deployments..."
for svc in api-gateway profile-service search-service booking-service \
           submission-service messaging-service live-session-service; do
  kubectl rollout restart deployment/"$svc" -n "$NAMESPACE"
done
kubectl rollout status deployment/api-gateway -n "$NAMESPACE" --timeout=120s

# ── Phase 8: Deploy Lambda functions ──────────────────────────────────────────
echo ""
echo ">>> Phase 8: Deploying Lambda functions..."

# Package and deploy GitHub Analysis Lambda
cd lambda/github-analysis && npm install --omit=dev && zip -r ../..//tmp/github-analysis.zip . && cd ../..
aws lambda create-function \
  --function-name hiresphere-github-analysis \
  --runtime nodejs20.x \
  --handler index.handler \
  --zip-file fileb:///tmp/github-analysis.zip \
  --role "arn:aws:iam::${ACCOUNT_ID}:role/hiresphere-profile-service-role" \
  --environment "Variables={GITHUB_TOKEN=${GITHUB_TOKEN:-},SUBMISSION_SERVICE_WEBHOOK_URL=http://$(kubectl get svc api-gateway -n hiresphere -o jsonpath='{.status.loadBalancer.ingress[0].hostname}')/api/submissions/webhook/analysis-complete}" \
  --region "$REGION" 2>/dev/null || \
aws lambda update-function-code \
  --function-name hiresphere-github-analysis \
  --zip-file fileb:///tmp/github-analysis.zip \
  --region "$REGION"

# Wire Lambda to SQS trigger
aws lambda create-event-source-mapping \
  --function-name hiresphere-github-analysis \
  --event-source-arn "$(aws sqs get-queue-attributes --queue-url "$GITHUB_QUEUE" --attribute-names QueueArn --query Attributes.QueueArn --output text)" \
  --batch-size 5 \
  2>/dev/null || echo "Event source mapping already exists"

# ── Phase 9: Print Viva endpoints ─────────────────────────────────────────────
echo ""
echo "========================================"
echo " Deployment Complete!"
echo "========================================"
ALB_HOST=$(kubectl get ingress hiresphere-ingress -n hiresphere \
  -o jsonpath='{.status.loadBalancer.ingress[0].hostname}' 2>/dev/null || echo "pending")
echo ""
echo "  ALB Endpoint     : http://$ALB_HOST"
echo "  API Base URL     : http://$ALB_HOST/api"
echo "  Cognito Pool ID  : $COGNITO_POOL_ID"
echo "  Cognito Client ID: $COGNITO_CLIENT_ID"
echo "  S3 Bucket        : $S3_BUCKET"
echo "  Coturn TURN      : turn:$COTURN_IP:3478"
echo ""
echo "  Update your React .env:"
echo "    REACT_APP_API_URL=$ALB_HOST"
echo "    REACT_APP_COGNITO_POOL_ID=$COGNITO_POOL_ID"
echo "    REACT_APP_COGNITO_CLIENT_ID=$COGNITO_CLIENT_ID"
echo "    REACT_APP_COGNITO_REGION=$REGION"
echo ""
echo "  Useful commands:"
echo "    kubectl get pods -n hiresphere"
echo "    kubectl logs -f deployment/api-gateway -n hiresphere"
echo "    kubectl get hpa -n hiresphere"
