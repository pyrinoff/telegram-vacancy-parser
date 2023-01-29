mkdir /opt/docker
cd /opt/docker || exit
wget -O dwld https://www.dropbox.com/s/178yupyzatzdhn3/deploy.tar.gz?dl=1
tar zxf dwld
bash docker_install.sh