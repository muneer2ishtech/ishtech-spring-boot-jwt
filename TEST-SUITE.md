# Test Suite

```
# Base URL
export BASE_URL="http://localhost:8080/api/v1"

```

## User Profile Update API

### Sign-ups for Test Users

```
# Sign-up for Admin
curl -w "\n%{http_code}\n" -X POST "$BASE_URL/auth/signup" -H "Content-Type: application/json" -d '{"email":"admin@example.com","password":"Test#1234","passwordConfirm":"Test#1234","firstName":"Admin","lastName":"Strator","acceptTermsConditions":true}'

# Sign-up for USER_ID_1
curl -w "\n%{http_code}\n" -X POST "$BASE_URL/auth/signup" -H "Content-Type: application/json" -d '{"email":"user1@example.com","password":"Password1!","passwordConfirm":"Password1!","firstName":"User1","lastName":"One1","acceptTermsConditions":true}'

# Sign-up for USER_ID_2
curl -w "\n%{http_code}\n" -X POST "$BASE_URL/auth/signup" -H "Content-Type: application/json" -d '{"email":"user2@example.com","password":"Password2!","passwordConfirm":"Password2!","firstName":"User2","lastName":"Two2","acceptTermsConditions":true}'

```

### Sign-ins for Test Users

```
# Sign-in and get token for Admin
echo "### Sign in as Admin ###"
curl -w "\n%{http_code}\n" -X POST "$BASE_URL/auth/signin" -H "Content-Type: application/json" -d '{"email":"admin@example.com","password":"Test#1234"}'

# Sign-in and get token for USER_ID_1
echo "### Sign in as User1 ###"
curl -s -w "\n%{http_code}\n" -X POST "$BASE_URL/auth/signin" -H "Content-Type: application/json" -d '{"email":"user1@example.com","password":"Password1!"}'

# Sign-in and get token for USER_ID_2
echo "### Sign in as User2 ###"
curl -s -w "\n%{http_code}\n" -X POST "$BASE_URL/auth/signin" -H "Content-Type: application/json" -d '{"email":"user2@example.com","password":"Password2!"}'
```

- Change role to ADMIN for required user
  - Safe to do below in `START TRANSACTION;` and end with `COMMIT;` or `ROLLBACK;` based on result

```sql
UPDATE ishtech_auth_dev_schema.t_user_role SET role_name = 'ADMIN'
  WHERE user_id = (SELECT id FROM ishtech_auth_dev_schema.t_user WHERE email = 'admin@example.com');

```

### Set Variables

- MANUAL STEP: Copy token and ID values from the sign-in responses above.

```
export   TA="eyJhbGciOiJIUzM4NCJ9.TODO ..."  # Admin token
export TU_1="eyJhbGciOiJIUzM4NCJ9.TODO ..."  # User1 token
export TU_2="eyJhbGciOiJIUzM4NCJ9.TODO ..."  # User2 token

export  ADMIN_ID=TODO  # Admin user ID (from signup response)
export USER_ID_1=TODO  # User1 ID (from signup response)
export USER_ID_2=TODO  # User2 ID (from signup response)
export USER_ID_3=9999  # Non-existent user for testing 404

```

#### Verify variables

```
echo "### Variables Set ###"
echo "ADMIN_ID:$ADMIN_ID"
echo "USER_ID_1:$USER_ID_1"
echo "USER_ID_2:$USER_ID_2"
echo "USER_ID_3:$USER_ID_3"

```

## Test Cases:

| Case | User                   | userId                | DTO id        | Expected   |
|-----:|------------------------|-----------------------|---------------|------------|
|  0   | None (unauthenticated) | me                    | null          | 401        |
|  1   | Normal (User1)         | me                    | null          | 200        |
|  2   | Normal (User1)         | me                    | User1 (own)   | 200        |
|  3   | Normal (User1)         | me                    | User2 (other) | 403        |
|  4   | Normal (User1)         | User1 (own numeric)   | null          | 200        |
|  5   | Normal (User1)         | User1 (own numeric)   | User1 (own)   | 200        |
|  6   | Normal (User1)         | User1 (own numeric)   | User2 (other) | 403        |
|  7   | Normal (User1)         | User2 (other numeric) | null          | 403        |
|  8   | Admin                  | me                    | null          | 200        |
|  9   | Admin                  | me                    | Admin (own)   | 200        |
| 10   | Admin                  | me                    | User2 (other) | 403        |
| 11   | Admin                  | Admin (own numeric)   | null          | 200        |
| 12   | Admin                  | Admin (own numeric)   | Admin (own)   | 200        |
| 13   | Admin                  | Admin (own numeric)   | User2 (other) | 403        |
| 14   | Admin                  | User2 (other numeric) | null          | 200        |
| 15   | Admin                  | User2 (other numeric) | User2 (match) | 200        |
| 16   | Admin                  | User3 (non-existent)  | User3 (match) | 404 / 500  |

```

# Unauthenticated - Case 0
echo "### Unauthenticated ###"
curl -s -o /dev/null -w "Case 0: %{http_code}\n" -X PUT "$BASE_URL/users/me" -H "Content-Type: application/json" -d '{"firstName":"User","lastName":"One"}'

# Normal user tests
echo "### Normal User Tests (User ID: $USER_ID_1) ###"
# Case 1: normal user & url userId=me & request body no id, expected 200
curl -s -o /dev/null -w "Case 1: %{http_code}\n" -X PUT "$BASE_URL/users/me" -H "Authorization: Bearer $TU_1" -H "Content-Type: application/json" -d '{"firstName":"User1.1","lastName":"One1.1"}'

# Case 2: normal user & url userId=me & request body id matches own, expected 200
curl -s -o /dev/null -w "Case 2: %{http_code}\n" -X PUT "$BASE_URL/users/me" -H "Authorization: Bearer $TU_1" -H "Content-Type: application/json" -d "{\"id\":$USER_ID_1,\"firstName\":\"User1.2\",\"lastName\":\"One1.2\"}"

# Case 3: normal user & url userId=me & request body id matches other user, expected 403
curl -s -o /dev/null -w "Case 3: %{http_code}\n" -X PUT "$BASE_URL/users/me" -H "Authorization: Bearer $TU_1" -H "Content-Type: application/json" -d "{\"id\":$USER_ID_2,\"firstName\":\"User1.3\",\"lastName\":\"One1.3\"}"

# Case 4: normal user & url userId=own numeric & request body no id, expected 200
curl -s -o /dev/null -w "Case 4: %{http_code}\n" -X PUT "$BASE_URL/users/$USER_ID_1" -H "Authorization: Bearer $TU_1" -H "Content-Type: application/json" -d "{\"firstName\":\"User1.4\",\"lastName\":\"One1.4\"}"

# Case 5: normal user & url userId=own numeric & request body id matches own, expected 200
curl -s -o /dev/null -w "Case 5: %{http_code}\n" -X PUT "$BASE_URL/users/$USER_ID_1" -H "Authorization: Bearer $TU_1" -H "Content-Type: application/json" -d "{\"id\":$USER_ID_1,\"firstName\":\"User1.5\",\"lastName\":\"One1.5\"}"

# Case 6: normal user & url userId=own numeric & request body id matches other user, expected 403
curl -s -o /dev/null -w "Case 6: %{http_code}\n" -X PUT "$BASE_URL/users/$USER_ID_1" -H "Authorization: Bearer $TU_1" -H "Content-Type: application/json" -d "{\"id\":$USER_ID_2,\"firstName\":\"User1.6\",\"lastName\":\"One1.6\"}"

# Case 7: normal user & url userId=other user numeric & request body no id, expected 403
curl -s -o /dev/null -w "Case 7: %{http_code}\n" -X PUT "$BASE_URL/users/$USER_ID_2" -H "Authorization: Bearer $TU_1" -H "Content-Type: application/json" -d "{\"firstName\":\"User1.7\",\"lastName\":\"One1.7\"}"

# Admin tests
echo "### Admin Tests (Admin ID: $ADMIN_ID) ###"
# Case 8: Admin & url userId=me & request body no id, expected 200
curl -s -o /dev/null -w "Case 8: %{http_code}\n" -X PUT "$BASE_URL/users/me" -H "Authorization: Bearer $TA" -H "Content-Type: application/json" -d "{\"firstName\":\"Admin8\",\"lastName\":\"Strator8\"}"

# Case 9: Admin & url userId=me & request body id matches own, expected 200
curl -s -o /dev/null -w "Case 9: %{http_code}\n" -X PUT "$BASE_URL/users/me" -H "Authorization: Bearer $TA" -H "Content-Type: application/json" -d "{\"id\":$ADMIN_ID,\"firstName\":\"Admin9\",\"lastName\":\"Strator9\"}"

# Case 10: Admin & url userId=me & request body id matches other user, expected 403
curl -s -o /dev/null -w "Case 10: %{http_code}\n" -X PUT "$BASE_URL/users/me" -H "Authorization: Bearer $TA" -H "Content-Type: application/json" -d "{\"id\":$USER_ID_2,\"firstName\":\"Admin10\",\"lastName\":\"Strator10\"}"

# Case 11: Admin & url userId=own numeric & request body no id, expected 200
curl -s -o /dev/null -w "Case 11: %{http_code}\n" -X PUT "$BASE_URL/users/$ADMIN_ID" -H "Authorization: Bearer $TA" -H "Content-Type: application/json" -d "{\"firstName\":\"Admin11\",\"lastName\":\"Strator11\"}"

# Case 12: Admin & url userId=own numeric & request body id matches own, expected 200
curl -s -o /dev/null -w "Case 12: %{http_code}\n" -X PUT "$BASE_URL/users/$ADMIN_ID" -H "Authorization: Bearer $TA" -H "Content-Type: application/json" -d "{\"id\":$ADMIN_ID,\"firstName\":\"Admin12\",\"lastName\":\"Strator12\"}"

# Case 13: Admin & url userId=own numeric & request body id matches other user, expected 403
curl -s -o /dev/null -w "Case 13: %{http_code}\n" -X PUT "$BASE_URL/users/$ADMIN_ID" -H "Authorization: Bearer $TA" -H "Content-Type: application/json" -d "{\"id\":$USER_ID_2,\"firstName\":\"Admin13\",\"lastName\":\"Strator13\"}"

# Case 14: Admin & url userId=other user & request body no id, expected 200
curl -s -o /dev/null -w "Case 14: %{http_code}\n" -X PUT "$BASE_URL/users/$USER_ID_2" -H "Authorization: Bearer $TA" -H "Content-Type: application/json" -d "{\"firstName\":\"User2.14\",\"lastName\":\"Two2.14\"}"

# Case 15: Admin & url userId=other user & request body id matches, expected 200
curl -s -o /dev/null -w "Case 15: %{http_code}\n" -X PUT "$BASE_URL/users/$USER_ID_2" -H "Authorization: Bearer $TA" -H "Content-Type: application/json" -d "{\"id\":$USER_ID_2,\"firstName\":\"User2.15\",\"lastName\":\"Two2.15\"}"

# Case 16: Admin & url userId=non-existent user & request body id matches, expected 404
curl -s -o /dev/null -w "Case 16: %{http_code}\n" -X PUT "$BASE_URL/users/$USER_ID_3" -H "Authorization: Bearer $TA" -H "Content-Type: application/json" -d "{\"id\":$USER_ID_3,\"firstName\":\"User3.16\",\"lastName\":\"Three3.16\"}"

echo;echo "### All tests complete ###"

```
