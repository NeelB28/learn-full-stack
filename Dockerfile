# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Install curl and netcat for health checks
RUN apt-get update && apt-get install -y curl netcat-openbsd && apt-get clean

# Copy the wait-for-it script
COPY --chmod=755 https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /usr/local/bin/wait-for-it.sh

# Copy the built JAR file into the container
COPY target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Create a startup script
RUN echo '#!/bin/sh \n\
# Wait for MySQL to be available \n\
wait-for-it.sh ${MYSQL_HOST:-mysql}:${MYSQL_PORT:-3306} -t 120 \n\
# Run the application \n\
java ${JAVA_OPTS:--Xmx512m -Xms256m} -jar app.jar \n\
' > /app/startup.sh && chmod +x /app/startup.sh

# Use the startup script as the entrypoint
ENTRYPOINT ["/app/startup.sh"]
