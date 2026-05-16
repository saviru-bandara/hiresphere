
#   docker-compose build profile-service
#
# Or manually:
#   docker build --build-arg SERVICE_NAME=profile-service \
#                --build-arg EXPOSE_PORT=8081 \
#                -t hiresphere/profile-service .

ARG SERVICE_NAME
ARG EXPOSE_PORT=8080

# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS build
ARG SERVICE_NAME

WORKDIR /workspace

# invalidated when a POM changes — not on every source code change.
COPY pom.xml                                    ./pom.xml
COPY services/${SERVICE_NAME}/pom.xml           ./services/${SERVICE_NAME}/pom.xml

# Download all dependencies declared in this service's POM
# (-pl = this module only, -am = include parent so relativePath resolves)
RUN mvn dependency:go-offline -pl services/${SERVICE_NAME} -am \
      -B --no-transfer-progress -q || true

COPY services/${SERVICE_NAME}/src  ./services/${SERVICE_NAME}/src
COPY proto/                        ./proto/

RUN mvn package -pl services/${SERVICE_NAME} -am \
      -DskipTests -B --no-transfer-progress

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
ARG SERVICE_NAME
ARG EXPOSE_PORT

WORKDIR /app

# Run as non-root
RUN addgroup -S hiresphere && adduser -S hiresphere -G hiresphere
USER hiresphere

COPY --from=build /workspace/services/${SERVICE_NAME}/target/*.jar app.jar

EXPOSE ${EXPOSE_PORT}

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
