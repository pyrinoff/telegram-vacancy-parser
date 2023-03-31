#!/bin/bash
#source docker_login.sh
docker login
docker-compose -f ./docker-compose.yml up -d