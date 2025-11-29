#!/bin/bash
DOCKER_USERNAME="nguyenminhquan2108"
API_GATEWAY_IMAGE="api-gateway"
CORE_SERVICE_IMAGE="core-service-cvconnect"
USER_SERVICE_IMAGE="user-service-cvconnect"
NOTIFY_SERVICE_IMAGE="notify-service-cvconnect"

echo "Pull Images"
docker pull $DOCKER_USERNAME/$API_GATEWAY_IMAGE:latest
docker pull $DOCKER_USERNAME/$CORE_SERVICE_IMAGE:latest
docker pull $DOCKER_USERNAME/$USER_SERVICE_IMAGE:latest
docker pull $DOCKER_USERNAME/$NOTIFY_SERVICE_IMAGE:latest

echo "Stop and Remove Existing Containers"
docker stop $API_GATEWAY_IMAGE || true
docker rm $API_GATEWAY_IMAGE || true
docker stop $CORE_SERVICE_IMAGE || true
docker rm $CORE_SERVICE_IMAGE || true
docker stop $USER_SERVICE_IMAGE || true
docker rm $USER_SERVICE_IMAGE || true
docker stop $NOTIFY_SERVICE_IMAGE || true
docker rm $NOTIFY_SERVICE_IMAGE || true

echo "Create Network if not exists"
docker network inspect cvconnect-network >/dev/null 2>&1 || docker network create cvconnect-network

echo "Run New Containers"
docker run -d --name $API_GATEWAY_IMAGE --network cvconnect-network --env-file /home/vclong2003/quannm32/config/cvconnect.env -p 8888:8888 $DOCKER_USERNAME/$API_GATEWAY_IMAGE:latest
docker run -d --name $USER_SERVICE_IMAGE --network cvconnect-network --env-file /home/vclong2003/quannm32/config/cvconnect.env -p 8180:8180 $DOCKER_USERNAME/$USER_SERVICE_IMAGE:latest
docker run -d --name $NOTIFY_SERVICE_IMAGE --network cvconnect-network --env-file /home/vclong2003/quannm32/config/cvconnect.env -p 8181:8181 $DOCKER_USERNAME/$NOTIFY_SERVICE_IMAGE:latest
docker run -d --name $CORE_SERVICE_IMAGE --network cvconnect-network --env-file /home/vclong2003/quannm32/config/cvconnect.env -p 8182:8182 $DOCKER_USERNAME/$CORE_SERVICE_IMAGE:latest

echo "Deployment Completed"
