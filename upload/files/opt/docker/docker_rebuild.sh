#!/bin/bash
docker-compose down --remove-orphans
docker-compose build -force-rm --no-cache
source docker_up.sh