# ishtech-spring-boot-jwt

## Versions
- java - 25
- spring-boot - 3.5.7


## Project structure

[ishtech-spring-boot-jwt](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt) - [README](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt/blob/main/README.md)<br>
├── [ishtech-spring-boot-jwt-lib](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt/tree/main/ishtech-spring-boot-jwt-lib) - [README](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt/blob/dev/ishtech-spring-boot-jwt-lib/README.md)<br>
└── [ishtech-spring-boot-jwt-web](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt/tree/main/ishtech-spring-boot-jwt-web) - [README](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt/blob/dev/ishtech-spring-boot-jwt-web/README.md)<br>


## APIs

| Module  | Type            | HTTP  | URL                          | Description |
|---------|-----------------|-------|------------------------------|-------------|
| API Doc | OpenAPI         | GET   | /v3/api-docs                 | Swagger generated API Documentation                 |
| API Doc | Swagger         | GET   | /swagger-ui.html             | Swagger Documentation Home                          |
| Auth    | Signin          | GET   | /api/v1/auth/signin          | Authenticate user with password and returns JWT     |
| Auth    | Signup          | GET   | /api/v1/auth/signup          | Registers new user                                  |
| Auth    | Update Password | GET   | /api/v1/auth/update-password | Updates password after signing in                   |
| Auth    | Forgot Password | GET   | /api/v1/auth/forgot-password | Creates and sends reset password link               |
| Auth    | Update Password using Reset Token| GET   | /api/v1/auth/reset-password | Sets new password using reset token |

- For details you can see swagger documentation
    - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- If running on different server or port change their values in URL


## Build & Run

### Local Build

#### Build using Maven
- You can make build with or without running tests

```
mvn clean package -DskipTests=true
```

#### To get source code and javadoc of dependencies

```
mvn dependency:resolve -Dclassifier=sources;javadoc

mvn dependency:tree

```


#### Docker build

```
docker build \
  --build-arg APP_VERSION=0.3.0-SNAPSHOT \
  --build-arg SERVER_PORT=8080 \
  -t ishtech-spring-boot-jwt-web:0.3.0-SNAPSHOT .
```

### Local Run

#### Run using Maven

```
./mvnw -pl ishtech-spring-boot-jwt-web spring-boot:run
```

#### Docker Run


```
 docker run -p 8080:8080 ishtech-spring-boot-jwt-web:0.3.0-SNAPSHOT
```
