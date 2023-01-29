#!/bin/bash

#install docker
bash docker_install.sh

#enable autostart service that we copy to /etc/systemd/system
systemctl enable docker-compose-autostart

#up docker
bash docker_up.sh