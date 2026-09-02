# ishtech-springboot-jwtauth
Spring Boot Auth using JWT - parent project

## Tech stack
- Java: 25
- Spring Boot: 4.0.x
- Security: JWT
- Database: Supports various databases, see child projects for details
- Database Migration: Flyway
- Containerization: Docker

##

[GIT](https://github.com/ishtech/ishtech-springboot-jwtauth)


## Design
- [ishtech-jpa-base](https://github.com/ishtech/ishtech-base-jpa) - Foundational JPA and other base classes

## Project structure

[ishtech-springboot-jwtauth](./README.md)<br>
├── [ishtech-springboot-jwtauth-lib](./ishtech-springboot-jwtauth-lib/README.md)<br>
├── [ishtech-springboot-jwtauth-api](./ishtech-springboot-jwtauth-api/README.md)<br>
└── [ishtech-springboot-jwtauth-web](./ishtech-springboot-jwtauth-web/README.md)<br>


## Usage

1. Add JWT Security to your project
    - If you only need Spring Boot security configuration with JWT, add the `ishtech-springboot-jwtauth-lib` module as a dependency (in your project `pom.xml` or `build.gradle`).

1. Add JWT Security + REST APIs (Signin, Signup, etc.)
    - If you need both JWT security and ready-to-use REST APIs for authentication, add the `ishtech-springboot-jwtauth-api` module as a dependency.

1. Run an independent Spring Boot authentication server
    - If you want a standalone authentication and authorization application, run the `ishtech-springboot-jwtauth-web` Spring Boot application.

## APIs

- For details you can see swagger documentation
    - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
    - [http://localhost:8080/v3/api-docs.yaml](http://localhost:8080/v3//v3/api-docs.yaml)

- Note: Check and update URI and PORT on which application is running

- For API names and descriptions:
    - See [API-INFO.md](./API-INFO.md)

- For `curl` & `json` request/response samples:
    - See [CURL-INFO.md](./CURL-INFO.md)


## Known Issues
- See [KNOWN-ISSUES.md](./KNOWN-ISSUES.md)


## Build and Run

- Ensure the port, db properties etc are correct in application-xxx.properties

### Maven

#### Local Maven Build

- Build without tests

```
./mvnw clean install -DskipTests
```

- Build with Junit tests

```
./mvnw clean install
```

#### Publish to Maven Central (Sonatype)

```
./mvnw clean deploy -DskipTests=true -pl "ishtech-springboot-jwtauth-lib,ishtech-springboot-jwtauth-api" -P gpg -P central-publishing -am
```
