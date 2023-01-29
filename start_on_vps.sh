#!/bin/bash

# Update the package list
sudo apt-get update

# Install fail2ban
sudo apt-get install fail2ban -y

# Create a copy of the default configuration file
sudo cp /etc/fail2ban/jail.conf /etc/fail2ban/jail.local

# Configure the jail for SSH
sudo bash -c "echo '[sshd]
enabled = true
port    = ssh
logpath = %(sshd_log)s
maxretry = 3' >> /etc/fail2ban/jail.local"

# Configure the jail for Apache
sudo bash -c "echo '[http-get-dos]
enabled = true
port    = http,https
filter  = http-get-dos
logpath = /var/log/apache2/access.log
maxretry = 300
findtime = 600
bantime = 3600' >> /etc/fail2ban/jail.local"

# Install Docker CE
sudo apt-get install apt-transport-https ca-certificates curl gnupg-agent software-properties-common -y
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io -y

# Restart fail2ban service
sudo service fail2ban restart

#git clone https://<username>:<password>@gitlab.com/<username>/<repository>.git
read -p "Enter your GitLab username: " username
read -sp "Enter your GitLab password: " password
echo

git clone https://$username:$password@gitlab.com/<username>/<repository>.git