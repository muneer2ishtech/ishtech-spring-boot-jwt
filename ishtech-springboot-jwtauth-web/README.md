# ishtech-springboot-jwtauth-web

`ishtech-springboot-jwtauth-web` provides Spring Boot applications with JWT based authentication.

## Overview

This is the **runnable module** — it exposes REST APIs for authentication and user management using JWT.


## Parent project

[ishtech-springboot-jwtauth](../README.md)


## Database
- See [DB-SETUP.md](./DB-SETUP.md) for setting up dev database


## Build and Run

- Ensure the port, db properties etc are correct in application-xxx.properties

### Maven

#### Local Maven Build

- For Build see [parent project](../README.md#Local Maven Build)

#### Local Maven Run

```
./mvnw -pl ishtech-springboot-jwtauth-web spring-boot:run -Dspring-boot.run.profiles=dev
```

### Docker

- See [DOCKER-BUILD.md](./DOCKER-BUILD.md)
