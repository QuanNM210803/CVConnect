#!/bin/bash
DOCKER_USERNAME="nguyenminhquan2108"
API_GATEWAY_IMAGE="api-gateway"
CORE_SERVICE_IMAGE="core-service"
USER_SERVICE_IMAGE="user-service"
NOTIFY_SERVICE_IMAGE="notify-service"

echo "Pull Images"
docker pull $DOCKER_USERNAME/$API_GATEWAY_IMAGE:latest
docker pull $DOCKER_USERNAME/$CORE_SERVICE_IMAGE:latest
docker pull $DOCKER_USERNAME/$USER_SERVICE_IMAGE:latest
docker pull $DOCKER_USERNAME/$NOTIFY_SERVICE_IMAGE:latest

echo "Stop and Remove Existing Containers"
docker stop api-gateway || true
docker rm api-gateway || true
docker stop core-service || true
docker rm core-service || true
docker stop user-service || true
docker rm user-service || true
docker stop notify-service || true
docker rm notify-service || true

echo "Create Network if not exists"
docker network inspect cvconnect-network >/dev/null 2>&1 || docker network create cvconnect-network

echo "Run New Containers"
docker run -d --name api-gateway --network cvconnect-network -p 8888:8888 $DOCKER_USERNAME/$API_GATEWAY_IMAGE:latest
docker run -d --name user-service --network cvconnect-network -p 8180:8080 $DOCKER_USERNAME/$USER_SERVICE_IMAGE:latest
docker run -d --name notify-service --network cvconnect-network -p 8181:8081 $DOCKER_USERNAME/$NOTIFY_SERVICE_IMAGE:latest
docker run -d --name core-service --network cvconnect-network -p 8182:8082 $DOCKER_USERNAME/$CORE_SERVICE_IMAGE:latest

echo "Deployment Completed"
