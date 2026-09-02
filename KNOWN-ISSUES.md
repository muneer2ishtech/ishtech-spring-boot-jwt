#

Known issues that have been confirmed by testing but not yet fixed. Each entry has enough detail to reproduce and fix without re-investigating.

---

## 1. `PUT /api/v1/users/{userId}` wipes fields that are absent from the request body

**Status:** Open
**Impact:** High — silent data loss on a documented API. A partial update (the exact request shown in `CURL-INFO.md`) clears `default_lang` and flips `t_user_profile.is_active` to `false`, with a `200 OK` and no warning.
**Affects:** `UserProfileMapper.toEntity(UserProfileDto, @MappingTarget UserProfile)` in `ishtech-springboot-jwtauth-lib`, used by `UserProfileServiceImpl.updateAndMapToDto(...)`

### Description

The update API is meant to be a partial update — `CURL-INFO.md` documents a request body containing only `id`, `firstName` and `lastName`. Any field not present in that body is deserialized as `null` in `UserProfileDto` and then written as `null` (or the primitive default) onto the loaded entity, overwriting the stored value.

Two confirmed symptoms from a single documented call:

1. **`default_lang` is cleared.** A user signed up with `"lang": "en"` has `default_lang = 'en'`. After one rename it is `NULL`.
2. **`t_user_profile.is_active` becomes `false`.** The profile row's `is_active` goes from `true` to `false`, so the user is effectively deactivated by a rename.

Symptom 2 also leaves the two active flags disagreeing: `t_user.is_active` stays `true` (it is a different column on a different table and is not touched by this mapper), while `t_user_profile.is_active` is now `false`. Any code reading one flag will disagree with code reading the other.

Both symptoms are genuinely persisted — this is not a stale-read or caching artifact. Reproduced on two independent databases: the local dev PostgreSQL and a throwaway `docker compose` PostgreSQL created from scratch.

### Steps to reproduce

```sh
# 1. signup
curl --request POST --location 'http://localhost:8080/api/v1/auth/signup' \
--header 'Content-Type: application/json' \
--data-raw '{"email":"x@example.com","password":"Test#1234","passwordConfirm":"Test#1234","firstName":"Api","lastName":"Test","acceptTermsConditions":true,"lang":"en"}'

# 2. signin, keep the access_token
curl --request POST --location 'http://localhost:8080/api/v1/auth/signin' \
--header 'Content-Type: application/json' \
--data-raw '{"email":"x@example.com","password":"Test#1234"}'

# 3. read it back -> "defaultLang":"en", "isActive":true
curl --request GET --location 'http://localhost:8080/api/v1/users/1' \
--header 'Authorization: Bearer <ACCESS_TOKEN>'

# 4. rename, exactly as documented in CURL-INFO.md
curl --request PUT --location 'http://localhost:8080/api/v1/users/1' \
--header 'Authorization: Bearer <ACCESS_TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{"id":1,"firstName":"New Api","lastName":"New Test"}'
```

Expected after step 4: `defaultLang` still `en`, `isActive` still `true`.
Actual: `"defaultLang":null`, `"isActive":false`.

Verify in the database:

```sql
SELECT u.id, u.email, u.is_active AS user_active, p.is_active AS profile_active, p.default_lang
  FROM ishtech_auth_dev_schema.t_user u
  JOIN ishtech_auth_dev_schema.t_user_profile p ON p.id = u.id
 ORDER BY u.id;
```

Rows that have been through the update show `profile_active = f` and `default_lang` empty, while never-updated rows show `t` and `en`.

### Likely cause

`UserProfileMapper.toEntity(...)` sets the null-handling strategy on a single property instead of on the whole bean:

```java
@InheritInverseConfiguration(name = "toBriefDto")
@Mapping(source = "id", target = "id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
UserProfile toEntity(UserProfileDto dto, @MappingTarget UserProfile entity);
```

`NullValuePropertyMappingStrategy.IGNORE` here applies only to `id`. Every other property inherited from `toBriefDto` (`middleName`, `nickName`, `title`, `prefix`, `suffix`, `defaultLang`, and the active flag) falls back to MapStruct's default `SET_TO_NULL`, so a null in the DTO overwrites whatever the loaded entity held. For the boolean active flag, a null `Boolean` mapped onto a primitive target lands as `false`, which is why the rename deactivates the profile.

`UserProfileServiceImpl.updateAndMapToDto(...)` loads the entity and passes it as `@MappingTarget`, which is correct — the merge semantics are what is wrong, not the lookup.

There is currently **no test coverage for the update path**. `UserControllerTest` contains only read tests (`testFindUserProfileByIdByAdmin`, `testFindUserProfileByUserIdBySelf`, `testFindUserProfileByMeBySelf`), which is presumably why this was never caught.

### Suggested fix

1. Move the null-handling to bean level on `toEntity`, e.g. `@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)`, so absent fields leave the loaded entity untouched. Keep `id` non-overwritable.
2. Decide deliberately whether `isActive` should be updatable through this API at all — if not, ignore it explicitly in the mapping rather than relying on null handling.
3. Clarify the relationship between `t_user.is_active` and `t_user_profile.is_active`; today they can silently diverge.
4. Add tests: update with only `firstName`/`lastName` preserves `defaultLang` and the active flag; the `PUT` response matches a subsequent `GET`; the persisted row matches the API response.
