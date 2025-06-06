# Etapa de compilación
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Construir la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Arrancar en el puerto asignado por Heroku
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}"]
