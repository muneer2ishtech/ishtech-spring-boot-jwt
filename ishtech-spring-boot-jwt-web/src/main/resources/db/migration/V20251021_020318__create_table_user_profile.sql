CREATE TABLE t_user_profile (
  id           INT          PRIMARY KEY,
  first_name   VARCHAR(255) NOT NULL,
  middle_name  VARCHAR(255)     NULL,
  last_name    VARCHAR(255) NOT NULL,
  default_lang VARCHAR(2)       NULL,
  CONSTRAINT fk_user_profile_user FOREIGN KEY (id) REFERENCES t_user(id)
);
