#

- Check and use right port for the API calls

## Swagger APIs

| Module  | Type    | HTTP  | URL              | Description |
|---------|-----------------|-------|------------------------------|-------------|
| API Doc | OpenAPI | GET   | /v3/api-docs     | Swagger generated API Documentation |
| API Doc | Swagger | GET   | /swagger-ui.html | Swagger Documentation Home          |


## Auth APIs
| Module | Type            | HTTP  | URL                          | Description |
|--------|-----------------|-------|------------------------------|-------------|
| Auth   | Signin          | POST  | /api/v1/auth/signin          | Authenticate user with password and returns JWT     |
| Auth   | Signup          | POST  | /api/v1/auth/signup          | Registers new user                                  |
| Auth   | Update Password | GET   | /api/v1/auth/update-password | Updates password after signing in                   |
| Auth   | Forgot Password | GET   | /api/v1/auth/forgot-password | Creates and sends reset password link               |
| Auth   | Update Password using Reset Token| GET   | /api/v1/auth/reset-password | Sets new password using reset token |

- For `curl` & `json` request/response samples:
    - See [CURL-INFO.md](./CURL-INFO.md)
