# __define-ocg__ Dockerfile for running a Java application named "MyApp"

# Use the OpenJDK 17 base image
FROM openjdk:17

# Copy the JAR file from the target directory to the Docker image
COPY target/*.jar app.jar

# Set the entry point to run the Java application
ENTRYPOINT ["java", "-jar", "/app.jar"]
