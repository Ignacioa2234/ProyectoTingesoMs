# 1. Imagen base con Java 17
FROM eclipse-temurin:17-jdk-jammy AS build

# 2. Directorio de trabajo
WORKDIR /app

# 3. Copiar pom y descargar dependencias (cacheable)
COPY pom.xml .
COPY settings.xml* .     # opcional si tienes settings para repos privados
RUN mvn dependency:go-offline -B

# 4. Copiar el código y compilar
COPY src ./src
RUN mvn package -DskipTests -B

# 5. Imagen de producción
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 6. Puerto que expondrá el contenedor, 0 deja al orquestador asignarlo
EXPOSE 8080

# 7. Comando de arranque
ENTRYPOINT ["java","-jar","/app/app.jar"]
