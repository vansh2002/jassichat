# ---- Build Stage ----
# Use an official Maven image with a specific JDK version for reproducible builds
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy only the pom.xml first to leverage Docker's layer caching.
# Dependencies will only be re-downloaded if pom.xml changes.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of your source code
COPY src ./src

# Package the application, skipping tests which aren't needed for a production build
RUN mvn clean package -DskipTests


# ---- Runtime Stage ----
# Use a lightweight, production-ready JRE image to keep the final image size small
FROM eclipse-temurin:17-jre-alpine

# Set the working directory for the runtime container
WORKDIR /app

# Copy the built JAR file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your application runs on (documentation for Render)
EXPOSE 8080

# The command to run your application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
