FROM ubuntu:latest
LABEL authors="acaer"
RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
COPY . .
RUN ./mvnw spring-boot:run

FROM openjdk:21-jdk-slim
EXPOSE 8080
COPY --from=target /target/spring-boot-jpa-h2-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
