FROM tomcat:9-jdk17-openjdk
#WORKDIR /opt
EXPOSE 8080
COPY target/jobstat.war /usr/local/tomcat/webapps
CMD ["catalina.sh", "run"]
