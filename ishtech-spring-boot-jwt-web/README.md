# ishtech-spring-boot-jwt-web

`ishtech-spring-boot-jwt-web` provides Spring Boot applications with JWT based authentication.

## Parent project

[ishtech-spring-boot-jwt](https://github.com/muneer2ishtech/ishtech-spring-boot-jwt) - [README](../README.md)

## Overview

This is the **runnable module** — it exposes REST APIs for authentication and user management using JWT.

## Tech stack
- PostgreSql:18
- Flyway for DB Migration

## Database

- `BIGINT` vs `INT` for `id` columns
     - `BIGINT` is `-2^63` to `2^63-1` (64 bits or 8 bytes)
     - `INT` is `-2^31` to `2^31-1` (32 bits or 4 bytes)
         - giving more than 2 billion positive values, enough for any app

### Dev DB setup
- Run following for first time before launching spring-boot-boot-app after PostgreSql is started

```
\connect ishtech_dev_db;

-- Note: CREATE USER is alias for CREATE ROLE WITH LOGIN
CREATE USER ishtech_dev_auth_user        PASSWORD 'ishtech_dev_auth_pass'        NOSUPERUSER;
CREATE USER ishtech_dev_auth_flyway_user PASSWORD 'ishtech_dev_auth_flyway_pass' NOSUPERUSER;

GRANT CONNECT ON DATABASE ishtech_dev_db TO ishtech_dev_auth_user;
GRANT CONNECT ON DATABASE ishtech_dev_db TO ishtech_dev_auth_flyway_user;

CREATE SCHEMA ishtech_dev_auth_schema;
CREATE SCHEMA ishtech_dev_auth_aud_schema;

GRANT USAGE ON SCHEMA public                      TO ishtech_dev_auth_user;
GRANT USAGE ON SCHEMA ishtech_dev_auth_schema     TO ishtech_dev_auth_user;
GRANT USAGE ON SCHEMA ishtech_dev_auth_aud_schema TO ishtech_dev_auth_user;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA ishtech_dev_auth_schema     TO ishtech_dev_auth_user;
GRANT SELECT, INSERT                 ON ALL TABLES IN SCHEMA ishtech_dev_auth_aud_schema TO ishtech_dev_auth_user;

-- Note: Future tables to inherit same privileges
ALTER DEFAULT PRIVILEGES IN SCHEMA ishtech_dev_auth_schema     GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO ishtech_dev_auth_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA ishtech_dev_auth_aud_schema GRANT SELECT, INSERT                 ON TABLES TO ishtech_dev_auth_user;

GRANT USAGE         ON SCHEMA public                      TO ishtech_dev_auth_flyway_user;
GRANT USAGE, CREATE ON SCHEMA ishtech_dev_auth_schema     TO ishtech_dev_auth_flyway_user;
GRANT USAGE, CREATE ON SCHEMA ishtech_dev_auth_aud_schema TO ishtech_dev_auth_flyway_user;

GRANT SELECT ON ALL TABLES IN SCHEMA ishtech_dev_auth_schema     TO ishtech_dev_auth_flyway_user;
GRANT SELECT ON ALL TABLES IN SCHEMA ishtech_dev_auth_aud_schema TO ishtech_dev_auth_flyway_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA ishtech_dev_auth_schema     GRANT SELECT ON TABLES TO ishtech_dev_auth_flyway_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA ishtech_dev_auth_aud_schema GRANT SELECT ON TABLES TO ishtech_dev_auth_flyway_user;

-- NOTE: DROP, INSERT, UPDATE, DELETE for Flyway can be added later if needed

```
