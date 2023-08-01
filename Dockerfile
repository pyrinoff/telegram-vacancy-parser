FROM tomcat:9-jdk17-openjdk
#WORKDIR /opt
EXPOSE 8080
COPY build/libs/chatjobparser.war /usr/local/tomcat/webapps/jobstat.war
CMD ["catalina.sh", "run"]
