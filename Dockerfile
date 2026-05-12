# ====== Stage 1: Build ======
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY . .

ARG MAVEN_CLI_OPTS="-B -q -s .mvn/settings.xml"

RUN chmod +x ./mvnw
RUN echo $(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout 2>/dev/null) > .projectVersion

RUN ./mvnw $MAVEN_CLI_OPTS clean install -DskipTests=true

# ====== Stage 2: Runtime ======
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/.projectVersion .projectVersion

RUN export APP_VERSION=$(cat .projectVersion)

ARG SERVER_PORT=8080
#ENV SERVER_PORT=${SERVER_PORT}
EXPOSE ${SERVER_PORT:-8080}

COPY --from=build /app/ishtech-springboot-jwtauth-web/target/ishtech-springboot-jwtauth-web-${APP_VERSION-*}.jar ishtech-springboot-jwtauth-web.jar

ENTRYPOINT ["java", "-jar", "ishtech-springboot-jwtauth-web.jar"]
