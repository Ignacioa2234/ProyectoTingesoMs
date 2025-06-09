# build stage
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copiamos el wrapper y el POM para cachear dependencias
COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copiamos el código y construimos
COPY src ./src
RUN ./mvnw package -DskipTests -B

# runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copiamos el JAR empaquetado
COPY --from=build /app/target/*.jar reportes-service.jar

# Exponemos el puerto (según tu bootstrap/config)
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/reportes-service.jar"]
