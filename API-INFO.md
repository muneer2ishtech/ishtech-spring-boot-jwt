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


## User APIs

| Module | Type                | HTTP  | URL                          | Description |
|--------|---------------------|-------|------------------------------|-------------|
| User   | Get Users Info      | GET   | /api/v1/users                | Retrieves paginated list of UserProfiles with filters. Only for ADMIN role |
| User   | Get User Info by Id | GET   | /api/v1/users/${userId}      | Gets UserProfile by userId             |
| User   | Update User by Id   | PUT   | /api/v1/users/${userId}      | Updates UserProfile by userId          |
| User   | Get Current User    | GET   | /api/v1/users/me             | Gets Gets UserProfile for current user |
| User   | Update Current User | PUT   | /api/v1/users/me             | Updates UserProfile for current user   |


- For `curl` & `json` request/response samples:
    - See [CURL-INFO.md](./CURL-INFO.md)
