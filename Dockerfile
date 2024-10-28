FROM openjdk:21-jdk-slim
LABEL authors="Aymane El Maini"

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/it-survey.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
