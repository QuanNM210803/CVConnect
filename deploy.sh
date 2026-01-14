#!/bin/bash
set -o allexport
source /home/quannm32/environment/cvconnect.env
set +o allexport

echo "Pull Images"
docker pull $DOCKER_USERNAME/$HOST_API_GATEWAY:latest
docker pull $DOCKER_USERNAME/$HOST_CORE_SERVICE:latest
docker pull $DOCKER_USERNAME/$HOST_USER_SERVICE:latest
docker pull $DOCKER_USERNAME/$HOST_NOTIFY_SERVICE:latest

echo "Stop and Remove Existing Containers"
docker stop $HOST_API_GATEWAY || true
docker rm $HOST_API_GATEWAY || true
docker stop $HOST_CORE_SERVICE || true
docker rm $HOST_CORE_SERVICE || true
docker stop $HOST_USER_SERVICE || true
docker rm $HOST_USER_SERVICE || true
docker stop $HOST_NOTIFY_SERVICE || true
docker rm $HOST_NOTIFY_SERVICE || true

echo "Create Network if not exists"
docker network inspect cvconnect-network >/dev/null 2>&1 || docker network create cvconnect-network

echo "Run New Containers"
docker run -d --restart=unless-stopped --name $HOST_API_GATEWAY --network cvconnect-network --env-file /home/quannm32/environment/cvconnect.env -p 8888:8888 $DOCKER_USERNAME/$HOST_API_GATEWAY:latest
docker run -d --restart=unless-stopped --name $HOST_USER_SERVICE --network cvconnect-network --env-file /home/quannm32/environment/cvconnect.env -p 8180:8180 $DOCKER_USERNAME/$HOST_USER_SERVICE:latest
docker run -d --restart=unless-stopped --name $HOST_NOTIFY_SERVICE --network cvconnect-network --env-file /home/quannm32/environment/cvconnect.env -p 8181:8181 $DOCKER_USERNAME/$HOST_NOTIFY_SERVICE:latest
docker run -d --restart=unless-stopped --name $HOST_CORE_SERVICE --network cvconnect-network --env-file /home/quannm32/environment/cvconnect.env -p 8182:8182 $DOCKER_USERNAME/$HOST_CORE_SERVICE:latest

echo "Deployment Completed"
