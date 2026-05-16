# ── Stage 1: Build React app ─────────────────────────────────────────────────
FROM node:20-alpine AS build
WORKDIR /app

# Copy package files first for layer caching
COPY frontend/package.json frontend/package-lock.json* ./
RUN npm install --legacy-peer-deps

# Copy source and build
COPY frontend/ .

# Build args injected at build time — override for production
ARG REACT_APP_API_URL=http://localhost:8080/api
ARG REACT_APP_WS_URL=ws://localhost:8086
ARG REACT_APP_TURN_URL=turn:localhost:3478
ARG REACT_APP_COGNITO_POOL_ID=local-pool
ARG REACT_APP_COGNITO_CLIENT_ID=local-client
ARG REACT_APP_COGNITO_REGION=us-east-1
ARG REACT_APP_S3_BUCKET=hiresphere-submissions-local
ARG REACT_APP_STRIPE_PUBLIC_KEY=pk_test_placeholder

ENV REACT_APP_API_URL=$REACT_APP_API_URL
ENV REACT_APP_WS_URL=$REACT_APP_WS_URL
ENV REACT_APP_TURN_URL=$REACT_APP_TURN_URL
ENV REACT_APP_COGNITO_POOL_ID=$REACT_APP_COGNITO_POOL_ID
ENV REACT_APP_COGNITO_CLIENT_ID=$REACT_APP_COGNITO_CLIENT_ID
ENV REACT_APP_COGNITO_REGION=$REACT_APP_COGNITO_REGION
ENV REACT_APP_S3_BUCKET=$REACT_APP_S3_BUCKET
ENV REACT_APP_STRIPE_PUBLIC_KEY=$REACT_APP_STRIPE_PUBLIC_KEY

RUN npm run build

# ── Stage 2: Serve with nginx ─────────────────────────────────────────────────
FROM nginx:1.25-alpine
WORKDIR /usr/share/nginx/html

# Remove default nginx content
RUN rm -rf ./*

# Copy built React app
COPY --from=build /app/build .

# Custom nginx config — handles React Router (SPA fallback to index.html)
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
