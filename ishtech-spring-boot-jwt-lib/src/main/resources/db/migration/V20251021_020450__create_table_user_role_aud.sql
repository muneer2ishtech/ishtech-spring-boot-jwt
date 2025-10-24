CREATE TABLE ${audit_schema_name}.t_user_role_aud (
  id            BIGINT        NOT NULL,
  rev           BIGINT        NOT NULL,
  revtype       SMALLINT      NOT NULL,
  rev_timestamp TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  user_id   BIGINT                NULL,
  role_name VARCHAR(255)          NULL,
  is_active BOOLEAN               NULL,
  PRIMARY KEY(id, rev)
);
