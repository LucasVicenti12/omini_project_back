FROM openjdk:17-jdk-alpine

WORKDIR /app

docker build -t nome-da-sua-imagem:tag .

COPY deploy/build/my_application.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
