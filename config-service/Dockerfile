# Usa Java 17 oficial
FROM openjdk:17

# Argumento para señalar el JAR que quieras copiar
ARG JAR_FILE=target/*.jar

# Copia el JAR empaquetado desde tu carpeta target
COPY ${JAR_FILE} app.jar

# Si quieres exponer un puerto dentro del contenedor (opcional, para la documentación)
EXPOSE 8081

# Comando de arranque
ENTRYPOINT ["java","-jar","/app.jar"]
