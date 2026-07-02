#

- Check and use the correct port for the API calls.

- For API names and descriptions:
    - See [API-INFO.md](./API-INFO.md)

# Auth APIs

## User Signup

### Request Details
- URL: `/api/v1/auth/signup`
- HTTP Method: `POST`

### Response Details
- HTTP Response Code: `201 - Created`
    - Response contains the newly created user `id`
- HTTP Response Code: `400 - Bad Request`
    - Returned if email already exists

### Request JSON

```json
{
    "email": "muneer@example.com",
    "password": "Test#1234",
    "passwordConfirm": "Test#1234",
    "firstName": "Muneer",
    "lastName": "Syed",
    "acceptTermsConditions": true,
    "lang": "en"
}
```

### Response JSON

```json
{
    "id": 1
}
```

### CURL

```sh
curl --request POST --location 'http://localhost:8080/api/v1/auth/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "muneer@example.com",
    "password": "Test#1234",
    "passwordConfirm": "Test#1234",
    "firstName": "Muneer",
    "lastName": "Syed",
    "acceptTermsConditions": true,
    "lang": "en"
}'
```

## User Signin

### Request Details
- URL: `/api/v1/auth/signin`
- HTTP Method: `POST`

### Response Details
- HTTP Response Code: `200 - OK`
    - Response contains JWT access token
- HTTP Response Code: `401 - Unauthorized`
    - Returned for invalid email or password

### Request JSON

```json
{
    "email": "muneer@example.com",
    "password": "Test#1234"
}
```

### Response JSON

```json
{
    "token_type": "Bearer",
    "access_token": "eyJhbGciOiJIUzI1NiJ9"
}
```

#### Sample JWT Payload

```json
{
    "sub": "muneer@example.com",
    "iat": 1693838542156,
    "exp": 1693840342156,
    "iss": "dev.jwtauth.springboot.ishtech.fi",
    "userId": 1,
    "roles": [
        "USER"
    ],
    "fullName": "Muneer Syed",
    "lang": "en"
}
```

### CURL

```sh
curl --request POST --location 'http://localhost:8080/api/v1/auth/signin' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "muneer@example.com",
    "password": "Test#1234"
}'
```

## Update Password

### Request Details
- URL: `/api/v1/auth/update-password`
- HTTP Method: `PUT`

### Response Details
- HTTP Response Code: `200 - OK`
    - Password updated successfully
- HTTP Response Code: `400 - Bad Request`
    - Returned for invalid request data
- HTTP Response Code: `401 - Unauthorized`
    - Returned for invalid or missing JWT token

### Request JSON

```json
{
    "password": "Test#6789",
    "passwordConfirm": "Test#6789"
}
```

### Response JSON

EMPTY

### CURL

```sh
curl --request PUT --location 'http://localhost:8080/api/v1/auth/update-password' \
--header 'Authorization: Bearer <ACCESS_TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{
    "password": "Test#6789",
    "passwordConfirm": "Test#6789"
}'
```

# User APIs

## Get Users

### Request Details
- URL: `/api/v1/users`
- HTTP Method: `GET`

### Response Details
- HTTP Response Code: `200 - OK`
- HTTP Response Code: `403 - Forbidden`
    - Returned if authenticated user does not have ADMIN role
- HTTP Response Code: `401 - Unauthorized`
    - Returned for invalid or missing JWT token

### Response JSON

```json
{
  "content": [
    ...
  ],
  "empty": false,
  "first": true,
  "last": true,
  "number": 0,
  "numberOfElements": 20,
  "pageable": {
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 20,
    "paged": true,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "unpaged": false
  },
  "size": 20,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "totalElements": 90,
  "totalPages": 5
}
```

- Content is array of UserProfile data, as in response of [Get User Details](#get-user-details)


## Get User Details

### Request Details
- URL: `/api/v1/users/{userId}`
- HTTP Method: `GET`

#### Path Variables

| Name   | Description |
|--------|-------------|
| userId | User ID     |

### Response Details
- HTTP Response Code: `200 - OK`
- HTTP Response Code: `403 - Forbidden`
    - Returned if authenticated user is not ADMIN or is trying to access another user's profile
- HTTP Response Code: `401 - Unauthorized`
    - Returned for invalid or missing JWT token

### Response JSON

```json
{
    "id": 1,
    "email": "muneer@example.com",
    "firstName": "Muneer",
    "lastName": "Syed"
}
```

### CURL

```sh
curl --request GET --location 'http://localhost:8080/api/v1/users/1' \
--header 'Authorization: Bearer <ACCESS_TOKEN>'
```

## Update User Details
- `email` cannot be updated using this API
- `password` cannot be updated using this API
- Request `id` must match authenticated user `id`

### Request Details
- URL: `/api/v1/users/{userId}`
- HTTP Method: `PUT`

#### Path Variables

| Name   | Description |
|--------|-------------|
| userId | User ID     |

### Response Details
- HTTP Response Code: `200 - OK`
- HTTP Response Code: `403 - Forbidden`
    - Returned if authenticated user is not ADMIN or is trying to update another user's profile
- HTTP Response Code: `401 - Unauthorized`
    - Returned for invalid or missing JWT token

### Request JSON

```json
{
    "id": 1,
    "firstName": "New Muneer",
    "lastName": "New Syed"
}
```

### Response JSON

```json
{
    "id": 1,
    "firstName": "New Muneer",
    "lastName": "New Syed"
}
```

### CURL

```sh
curl --request PUT --location 'http://localhost:8080/api/v1/users/1' \
--header 'Authorization: Bearer <ACCESS_TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 1,
    "firstName": "New Muneer",
    "lastName": "New Syed"
}'
```
