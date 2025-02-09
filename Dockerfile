
#FROM ubuntu:latest
#LABEL authors="acaer"
#RUN apt-get update
#RUN apt-get install openjdk-21-jdk -y
#COPY . .
#RUN ./mvnw spring-boot:run

#FROM openjdk:21-jdk-slim
#EXPOSE 8080
#COPY --from=target /target/spring-boot-jpa-h2-0.0.1-SNAPSHOT.jar app.jar

#ENTRYPOINT ["java", "-jar", "app.jar"]

# Usar una imagen base de OpenJDK 17 con JDK 17
FROM openjdk:17-jdk-alpine

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR del proyecto a la imagen Docker
COPY target/spring-boot-jpa-h2-0.0.1-SNAPSHOT.jar /app/spring-boot-jpa-h2-0.0.1-SNAPSHOT.jar

# Exponer el puerto en el que se ejecutará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "spring-boot-jpa-h2-0.0.1-SNAPSHOT.jar"]