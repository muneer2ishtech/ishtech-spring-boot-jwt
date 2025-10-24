# ishtech-spring-boot-jwt-lib

`ishtech-spring-boot-jwt-lib` provides reusable JWT-based authentication and security components for Spring Boot applications.

## Parent project

[ishtech-spring-boot-jwt](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt) - [README](../README.md)

## Overview

It can be included as a **Maven dependency** in any other Spring Boot project to instantly enable:
- User authentication with JWT tokens  
- Password management (update, reset, forgot)  
- Role and authority handling  
- Integration with Spring Security  

This module **does not run** as a standalone Spring Boot application.

---

## How to use

### Maven Dependency

To use this library in another project, add:

```xml
<dependency>
    <groupId>fi.ishtech.practice</groupId>
    <artifactId>ishtech-spring-boot-jwt-lib</artifactId>
    <version>x.y.z</version>
</dependency>
```

- Note: Replace `x.y.z` with appropriate version number, e.g. `1.0.0` or `1.1.0-SNAPSHOT`
