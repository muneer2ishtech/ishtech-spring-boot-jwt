# ishtech-spring-boot-jwt

## Versions
- java - 25
- spring-boot - 3.5.7


## Parent project

[ishtech-spring-boot-jwt](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt) - [README](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt/blob/main/README.md)
├── [ishtech-spring-boot-jwt-lib](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt/tree/main/ishtech-spring-boot-jwt-lib) - [README](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt/blob/dev/ishtech-spring-boot-jwt-lib/README.md)
└── [ishtech-spring-boot-jwt-web](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt/tree/main/ishtech-spring-boot-jwt-lib) - [README](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt/blob/dev/ishtech-spring-boot-jwt-lib/README.md)


## APIs

| Module  | Type            | HTTP  | URL                          | Description |
|---------|-----------------|-------|------------------------------|-------------|
| API Doc | OpenAPI         | GET   | /v3/api-docs                 | Swagger generated API Documentation                 |
| API Doc | Swagger         | GET   | /swagger-ui.html             | Swagger Documentation Home                          |
| Auth    | Signin          | GET   | /api/v1/auth/sigin           | Authenticate user with password and returns JWT     |
| Auth    | Signup          | GET   | /api/v1/auth/sigup           | Registers new user                                  |
| Auth    | Update Password | GET   | /api/v1/auth/update-password | Updates password after signing in                   |
| Auth    | Forgot Password | GET   | /api/v1/auth/forgot-password | Creates and sends reset password link               |
| Auth    | Update Password using Reset Token| GET   | /api/v1/auth/reset-password | Sets new password using reset token |

- For details you can see swagger documentation
    - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- If running on different server or port change their values in URL


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
