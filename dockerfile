# Etapa 1: Construcción
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar los archivos de configuración de Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar y empaquetar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-alpine

# Crear un usuario no-root para ejecutar la aplicación
RUN addgroup -S spring && adduser -S spring -G spring

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR de la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Cambiar al usuario no-root
USER spring:spring

# Exponer el puerto de la aplicación
EXPOSE 3005

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
```

