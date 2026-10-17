# Use official OpenJDK image as base
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml
mvnw pom.xml ./
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Run the jar file
CMD ["java", "-jar", "target/*.jar"]