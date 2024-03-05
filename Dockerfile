FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/myChat-0.0.1-SNAPSHOT-plain.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
