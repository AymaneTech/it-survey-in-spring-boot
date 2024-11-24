FROM maven:3.9.2-eclipse-temurin-17-alpine AS maven
WORKDIR /app
COPY ./pom.xml ./pom.xml

COPY ./src .

RUN mvn dependency:go-offline -B

RUN mvn clean package -X

FROM openjdk:22-jdk

WORKDIR /app
COPY --from=maven /app/target/jars/untitled.jar .


CMD ["java", "-jar", "untitled.jar"]