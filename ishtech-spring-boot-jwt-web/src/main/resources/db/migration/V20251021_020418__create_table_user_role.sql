CREATE TABLE t_user_role (
  id        SERIAL       PRIMARY KEY,
  user_id   INT          NOT NULL,
  role_name VARCHAR(255) NOT NULL,
  is_active BOOLEAN      NOT NULL DEFAULT TRUE,
  CONSTRAINT uk_user_role_user_id_role UNIQUE (user_id, role_name),
  CONSTRAINT fk_user_role_user         FOREIGN KEY (user_id) REFERENCES t_user(id)
);
