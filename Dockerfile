# Use Eclipse Temurin base image for Java 21
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
# Replace this with your actual jar name if different
COPY target/hunt-service-*.jar app.jar

# Expose the port your app listens on
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
