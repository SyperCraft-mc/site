FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/Obsidian-1.0.1.jar app.jar
EXPOSE 8888
CMD ["java", "-jar", "app.jar"]