# Use Maven with OpenJDK 17 to build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy only the pom.xml file first to leverage Docker cache
COPY UrbanPetHouse/pom.xml .  

# Copy the entire src directory
COPY UrbanPetHouse/src ./src  

# Build the application
RUN mvn clean package -DskipTests

# Use a lightweight OpenJDK image to run the application
FROM openjdk:17.0.1-jdk-slim

# Copy the built jar file from the build stage
COPY --from=build /app/target/UrbanPetHouse-0.0.1-SNAPSHOT.jar UrbanPetHouse.jar

# Expose the application port
EXPOSE 8080

# Define the entry point for the application
ENTRYPOINT ["java", "-jar", "UrbanPetHouse.jar"]
