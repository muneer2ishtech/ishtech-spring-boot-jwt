FROM openjdk:21

ENV FI_ISTECH_FILES_LOCAL_ROOT-FOLDER=/tmp

EXPOSE 8080

COPY target/ishtech-spring-boot-jwt-0.0.1-SNAPSHOT.jar ishtech-spring-boot-jwt.jar

ENTRYPOINT ["java","-jar","/ishtech-spring-boot-jwt.jar"]
