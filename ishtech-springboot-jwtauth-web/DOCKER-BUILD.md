## Docker

- `docker` commands should be run from [parent project root folder](../)

### Docker build

- arg for custom `SERVER_PORT` is optional, you can change to desired value or skip, if skipped it will use default `8080`

```
docker build . \
  -t "muneer2ishtech/ishtech-springboot-jwtauth-web:$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout 2>/dev/null)"

```

- With custom `SERVER_PORT`

```
docker build . \
  --build-arg SERVER_PORT=8181 \
  -t "muneer2ishtech/ishtech-springboot-jwtauth-web:$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout 2>/dev/null)"

```

### Run with docker image

- Note: check and use version from pom.xml
  - Replace `x.y.z` with appropriate version number, e.g. `1.0.0` or `1.1.0-SNAPSHOT`

- Add option ` -d` if you want to run in background

- Here are some of properties required
  - `SPRING_PROFILES_ACTIVE=dev`
  - `SPRING_DATASOURCE_URL=jdbc:postgresql://<db-host>:<db-port>/<db-name>`
  - `SPRING_DATASOURCE_USERNAME=<db-user>`
  - `SPRING_DATASOURCE_PASSWORD=<db-password>`
  - `FI_ISHTECH_SPRINGBOOT_JWTAUTH_JWT_SECRET=<jwt-secret>`

- If connecting to your own PostgreSQL instance (not docker compose), it must already have the app DB user/schema set up
  - See [DB-SETUP.md / Local](DB-SETUP.md#local)

- To run on default port and other default settings  
  - E.g.: Default port: `8080`

```
docker run \
  -e SPRING_PROFILES_ACTIVE=dev \
  -p 8080:8080 \
  muneer2ishtech/ishtech-springboot-jwtauth-web:x.y.z
```

- To run by connecting to PostgreSQL running on Docker

```
docker run \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres_dev:5432/ishtech_dev_db \
  --network postgres-18_default \
  -p 8080:8080 \
  muneer2ishtech/ishtech-springboot-jwtauth-web:x.y.z
```

```
docker run \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:15432/ishtech_dev_db \
  -p 8080:8080 \
  muneer2ishtech/ishtech-springboot-jwtauth-web:x.y.z
```

- If `host.docker.internal` doesn't resolve by default then `--add-host=host.docker.internal:host-gateway`


- To run by exposing on a different port  
  - Example: expose on `8282` (container still runs on `8080`)

```
docker run \
  ...
  -p 8282:8080 \
  muneer2ishtech/ishtech-springboot-jwtauth-web:x.y.z
```

- To run with custom application port inside container  
  - E.g.: Spring Boot runs on `8181`, exposed on `8282`

```
docker run \
  ...
  -e SERVER_PORT=8181 \
  -p 8282:8181 \
  muneer2ishtech/ishtech-springboot-jwtauth-web:x.y.z
```


### Run with docker compose

- Docker compose is self contained and has both spring-boot application and db is present, so  you don't need anything else other than docker

- To stop if running
    - `docker compose stop`

- To stop and remove including volumes and built images
    - `docker compose down -v --rmi=local`

- To build and start
    - You can prefix with env vars as in below example
    - Below args are optional, you can change to desired value or skip, if skipped they will use default value
        - `DB_PORT` if skipped DB will be exposed on default `5432`
        - `SERVER_PORT_REMOTE` if skipped spring-boot app will run on default `8080`
        - `SERVER_PORT_LOCAL` if skipped spring-boot app will be exposed on default `8080`

```
SERVER_PORT_LOCAL=8181 \
DB_PORT=25432 \
APP_VERSION=$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout 2>/dev/null) \
docker compose up --build

```
