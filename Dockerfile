# Используем официальный образ OpenJDK с нужной версией Java
FROM openjdk:17-jdk-alpine

# Указываем рабочую директорию внутри контейнера
WORKDIR /opt

# Открываем порт 8080 для приложения
EXPOSE 8080

# Копируем zip файл приложения в рабочую директорию
COPY /build/distributions/chatjobparser-boot-1.0-SNAPSHOT.zip /opt/chatjobparser-boot-1.0-SNAPSHOT.zip

# Распаковываем zip файл
RUN unzip chatjobparser-boot-1.0-SNAPSHOT.zip

# Указываем команду для запуска Spring Boot приложения
CMD ["sh", "/opt/chatjobparser-boot-1.0-SNAPSHOT/bin/chatjobparser"]
#CMD ["ls", "-ls"]
