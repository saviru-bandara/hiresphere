# ── Stage 1: Build ────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy parent POM and service POM first (layer cache for deps)
COPY pom.xml /workspace/parent-pom.xml
COPY services/api-gateway/pom.xml services/api-gateway/pom.xml

# Download dependencies (cached unless pom changes)
RUN mvn -f parent-pom.xml -N dependency:go-offline || true
RUN mvn dependency:go-offline -q || true

# Copy source and build
COPY services/api-gateway/src services/api-gateway/src
WORKDIR /workspace/services/api-gateway
RUN mvn clean package -DskipTests -q

# ── Stage 2: Runtime ──────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Non-root user for security
RUN addgroup -S hiresphere && adduser -S hiresphere -G hiresphere
USER hiresphere

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080 9090

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
