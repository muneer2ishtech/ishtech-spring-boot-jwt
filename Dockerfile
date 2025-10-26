# ====== Stage 1: Build ======
FROM eclipse-temurin:25-jdk AS build

COPY . .

RUN ./mvnw clean install -DskipTests=true

# ====== Stage 2: Runtime ======
FROM eclipse-temurin:25-jre

ARG APP_VERSION=0.4.0
ARG SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

COPY --from=build ishtech-spring-boot-jwt-web/target/ishtech-spring-boot-jwt-web-${APP_VERSION}.jar ishtech-spring-boot-jwt-web.jar

ENTRYPOINT ["java", "-jar", "ishtech-spring-boot-jwt-web.jar"]
