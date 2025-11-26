FROM maven:3.9.11-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml /app

RUN mvn dependency:go-offline -B

COPY . /app

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21.0.9_10-jdk-ubi9-minimal

EXPOSE 8080

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]