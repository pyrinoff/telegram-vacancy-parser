#!/bin/bash
docker-compose down --remove-orphans
#docker rm $(docker ps | awk '/chat-job-parser/ {print $1}')
docker rmi $(docker images -a | awk '/chat-job-parser/ {print $3}')
docker-compose build -force-rm --no-cache --pull
source docker_up.sh
