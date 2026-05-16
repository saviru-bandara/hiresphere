# HireSphere — Cloud-Native Interview Simulation Platform
SE6020 Cloud Computing Assignment — 2026 Semester 03

## Project Structure
```
hiresphere/
├── infrastructure/         # CloudFormation stack
├── proto/                  # gRPC Protobuf contracts (shared)
├── services/
│   ├── api-gateway/        # Spring Cloud Gateway
│   ├── profile-service/    # Profiles, badges, ratings (DynamoDB)
│   ├── search-service/     # Interviewer search (gRPC → Profile)
│   ├── booking-service/    # Bookings & payments (RDS PostgreSQL)
│   ├── submission-service/ # Code uploads & GitHub (S3 + SQS)
│   ├── messaging-service/  # Direct messaging (DynamoDB)
│   └── live-session-service/ # WebRTC signaling (WebSocket)
├── lambda/
│   ├── github-analysis/    # SQS-triggered repo analysis (Node.js)
│   └── email-notification/ # SQS-triggered email via SES (Node.js)
├── k8s/                    # Kubernetes manifests
├── frontend/               # React + AWS Amplify
└── scripts/                # Build, deploy, local dev scripts

## Quick Start (Local Dev)
```bash
# 1. Start local infrastructure (LocalStack + PostgreSQL + Redis)
docker-compose up -d

# 2. Build all services
./scripts/build-all.sh

# 3. Run a single service
cd services/profile-service && mvn spring-boot:run
```

## AWS Deployment
```bash
# 1. Provision infrastructure
aws cloudformation deploy \
  --template-file infrastructure/hiresphere-infra.yaml \
  --stack-name hiresphere-viva \
  --capabilities CAPABILITY_NAMED_IAM \
  --parameter-overrides DBPassword=<your-password>

# 2. Authenticate kubectl
aws eks update-kubeconfig --name hiresphere-cluster --region us-east-1

# 3. Build & push Docker images
./scripts/build-and-push.sh

# 4. Deploy to Kubernetes
kubectl apply -f k8s/
```
