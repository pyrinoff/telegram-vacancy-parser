# Используем официальный образ OpenJDK с нужной версией Java
FROM openjdk:17-jdk-alpine

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Открываем порт 8080 для приложения
EXPOSE 8080

# Копируем jar файл приложения в рабочую директорию
COPY build/libs/chatjobparser.jar /app/chatjobparser.jar

# Указываем команду для запуска Spring Boot приложения
CMD ["java", "-jar", "/app/chatjobparser.jar"]