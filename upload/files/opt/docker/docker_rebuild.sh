#!/bin/bash
docker-compose down --remove-orphans
docker rm $(docker ps -a | awk '/chat-job-parser/ {print $1}')
docker-compose build -force-rm --no-cache --pull
source docker_up.sh
