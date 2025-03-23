# BUILDER stage - create an image that will build the Springboot app (ignore tests)
# Maven 3.9, Java 21
FROM maven:3.9-eclipse-temurin-21 AS BUILDER
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# RUNNER stage - create an image that will run the Springboot app
# Java 21 base image
FROM openjdk:21 AS RUNNER
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
