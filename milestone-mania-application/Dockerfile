# Dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app
COPY target/milestone-mania-application-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

