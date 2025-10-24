CREATE TABLE ${audit_schema_name}.t_user_aud (
  id                    INT          NOT NULL,
  rev                   INT          NOT NULL,
  revtype               SMALLINT     NOT NULL,
  rev_timestamp         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  username              VARCHAR(255)     NULL,
  email                 VARCHAR(255)     NULL,
  password_hash         VARCHAR(255)     NULL,
  password_reset_token  VARCHAR(255)     NULL,
  force_change_password BOOLEAN          NULL,
  is_email_verified     BOOLEAN          NULL,
  is_active             BOOLEAN          NULL,
  PRIMARY KEY(id, rev)
);
