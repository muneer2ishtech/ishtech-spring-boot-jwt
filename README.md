# ishtech-spring-boot-jwt

## Versions
- java - 21
- spring-boot - 3.2.3


## Parent project
- See [ishtech-spring-boot-root](https://github.com/muneer2ishtech/ishtech-spring-boot-root) project and it's [README](https://github.com/muneer2ishtech/ishtech-spring-boot-root/blob/main/README.md)

## APIs

- Here is brief info APIs
- For details you can see swagger documentation
    - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- If running on different server or port change their values in URL


| Module  | Type                      | Auth | HTTP  | URL                          | Description |
|---------|---------------------------|------|-------|------------------------------|-------------|
| API Doc | OpenAPI                   | None | GET   | /v3/api-docs                 | Spring generated API Documentation  |
| API Doc | Swagger                   | None | GET   | /swagger-ui.html             | Swagger generated API Documentation |


# Build & Run

## Test
### JUnit Test
- If required change values in `src/test/resources/application.properties` 
- or pass as CLI parameters as below
    - You can pass multiple params `-Dprop1=val1 -Dprop2=val2` etc.

```
mvn test -Dprop1=val1
```

- To run single test with CLI params


```
mvn test -Dtest=fi.ishtech.springboot.controller.HomeControllerTest#testHome
```

### Local Build
### Build using Maven
- You can make build with or without running tests

```
mvn clean package -DskipTests=true
```

### Docker build
```
docker build -f Dockerfile . -t muneer2ishtech/ishtech_spring_boot_jwt:0.0.1-SNAPSHOT
```

### To get source code and javadoc of dependencies
```
mvn dependency:sources
mvn dependency:resolve -Dclassifier=javadoc
```

## Local Run
### Run using Maven

- You can override AWS config properties using CLI params as below
    - AWS secretKey is must

```
mvn spring-boot:run
```

### Run using Jar
```
java -jar ishtech-spring-boot-jwt-0.0.1-SNAPSHOT.jar
```

## Docker Run
### Pull image
```
docker pull muneer2ishtech/ishtech_spring_boot_jwt:0.0.1-SNAPSHOT
```

### Run using already built / pulled Docker image
```
docker run -it muneer2ishtech/ishtech_spring_boot_jwt:0.0.1-SNAPSHOT
```

## cURL

```
curl --request GET --location 'http://localhost:8080/'
```
