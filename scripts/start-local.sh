#!/bin/bash
# ─────────────────────────────────────────────────────────────────────────────
# HireSphere — Local Development Start
# Starts LocalStack + Postgres + Redis, then builds and starts all services
# ─────────────────────────────────────────────────────────────────────────────
set -euo pipefail

echo "=== Starting HireSphere local dev environment ==="

# Start infrastructure services only
echo ">>> Starting infrastructure (LocalStack, PostgreSQL, Redis)..."
docker-compose up -d localstack postgres redis

echo ">>> Waiting for LocalStack to be ready..."
until curl -sf http://localhost:4566/_localstack/health > /dev/null 2>&1; do
  echo "  Waiting..."
  sleep 3
done
echo "  LocalStack ready."

echo ">>> Waiting for PostgreSQL..."
until docker exec hiresphere-postgres pg_isready -U hsadmin -d hiresphere > /dev/null 2>&1; do
  sleep 2
done
echo "  PostgreSQL ready."

# Build all Maven modules
echo ""
echo ">>> Building Spring Boot services..."
mvn clean package -DskipTests -q

echo ""
echo "=== Infrastructure ready. Start services with: ==="
echo ""
echo "  # Run all services in Docker:"
echo "  docker-compose up -d"
echo ""
echo "  # OR run a single service locally (faster iteration):"
echo "  cd services/profile-service && SPRING_PROFILES_ACTIVE=local mvn spring-boot:run"
echo ""
echo "  # Run frontend:"
echo "  #cd frontend && npm start"
echo ""
echo "  LocalStack Dashboard : http://localhost:4566"
echo "  API Gateway          : http://localhost:8080"
