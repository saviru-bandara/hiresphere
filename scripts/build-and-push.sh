#!/bin/bash
# ─────────────────────────────────────────────────────────────────────────────
# HireSphere — Build & Push all Docker images to ECR
#
# Usage:
#   ./scripts/build-and-push.sh                   # builds + pushes all
#   ./scripts/build-and-push.sh profile-service   # builds + pushes one
#
# Prerequisites: AWS CLI configured, Docker running, ECR repos exist
# ─────────────────────────────────────────────────────────────────────────────
set -euo pipefail

REGION=${AWS_REGION:-us-east-1}
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
ECR_BASE="${ACCOUNT_ID}.dkr.ecr.${REGION}.amazonaws.com/hiresphere"
TAG=${GIT_SHA:-$(git rev-parse --short HEAD 2>/dev/null || echo "latest")}

SERVICES=(
  api-gateway
  profile-service
  search-service
  booking-service
  submission-service
  messaging-service
  live-session-service
)

# Allow building a single service if passed as arg
if [ $# -gt 0 ]; then
  SERVICES=("$1")
fi

echo "=== HireSphere Build & Push ==="
echo "  Account : $ACCOUNT_ID"
echo "  Region  : $REGION"
echo "  Tag     : $TAG"
echo ""

# Login to ECR
echo ">>> Logging in to ECR..."
aws ecr get-login-password --region "$REGION" | \
  docker login --username AWS --password-stdin "${ACCOUNT_ID}.dkr.ecr.${REGION}.amazonaws.com"

# Build Maven (parent + all modules) once — faster than per-service builds
echo ">>> Building all Maven modules..."
mvn clean package -DskipTests -q

for svc in "${SERVICES[@]}"; do
  echo ""
  echo ">>> [$svc] Building Docker image..."
  IMAGE="${ECR_BASE}/${svc}"

  docker build \
    -f "services/${svc}/Dockerfile" \
    -t "${IMAGE}:${TAG}" \
    -t "${IMAGE}:latest" \
    .

  echo ">>> [$svc] Pushing to ECR..."
  docker push "${IMAGE}:${TAG}"
  docker push "${IMAGE}:latest"

  echo ">>> [$svc] Done: ${IMAGE}:${TAG}"
done

echo ""
echo "=== All images pushed successfully ==="
