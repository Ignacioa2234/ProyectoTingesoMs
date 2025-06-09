# Etapa 1: compilar con Maven
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copiamos el wrapper y el pom para cachear dependencias
COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml .

# Descargamos dependencias sin compilar todo el proyecto
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copiamos el código fuente y construimos el JAR
COPY src ./src
RUN ./mvnw package -DskipTests -B

# Etapa 2: runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copiamos el JAR ya empacado
COPY --from=build /app/target/*.jar config-service.jar

# Exponemos el puerto que usará Spring Boot (el que tengas en application.yml)
EXPOSE 8081

# Arrancamos la aplicación
ENTRYPOINT ["java","-jar","/app/config-service.jar"]
