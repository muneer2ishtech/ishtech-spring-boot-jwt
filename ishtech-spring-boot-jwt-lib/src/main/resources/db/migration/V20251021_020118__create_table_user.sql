CREATE TABLE t_user (
  id                    BIGSERIAL    PRIMARY KEY,
  username              VARCHAR(255) NOT NULL,
  email                 VARCHAR(255) NOT NULL,
  password_hash         VARCHAR(255) NOT NULL,
  password_reset_token  VARCHAR(255)     NULL,
  force_change_password BOOLEAN          NULL DEFAULT FALSE,
  is_email_verified     BOOLEAN          NULL DEFAULT FALSE,
  is_active             BOOLEAN      NOT NULL DEFAULT TRUE,
  CONSTRAINT uk_user_email    UNIQUE (email),
  CONSTRAINT uk_user_username UNIQUE (username)
);
