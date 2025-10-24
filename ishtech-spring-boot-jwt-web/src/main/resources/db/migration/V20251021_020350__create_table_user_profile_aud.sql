CREATE TABLE ${audit_schema_name}.t_user_profile_aud (
  id            BIGINT       NOT NULL,
  rev           BIGINT       NOT NULL,
  revtype       SMALLINT     NOT NULL,
  rev_timestamp TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  first_name    VARCHAR(255) NOT NULL,
  middle_name   VARCHAR(255)     NULL,
  last_name     VARCHAR(255) NOT NULL,
  default_lang  VARCHAR(2)       NULL,
  PRIMARY KEY(id, rev)
);
