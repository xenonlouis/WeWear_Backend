# Use the official Eclipse Temurin (formerly AdoptOpenJDK) image
FROM eclipse-temurin:17-jdk-focal

# Set working directory
WORKDIR /app

# Set JAVA_HOME explicitly
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Copy the Maven wrapper files
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Give execute permissions to mvnw
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the project source
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Get the name of the built JAR file
RUN mv target/*.jar target/app.jar

# Run the application
ENTRYPOINT ["java","-jar","/app/target/app.jar"] 