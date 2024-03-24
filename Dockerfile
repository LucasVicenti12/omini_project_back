FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/my_application.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
