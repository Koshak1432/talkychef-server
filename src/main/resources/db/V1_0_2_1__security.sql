ALTER TABLE users
    ADD COLUMN login    varchar(64) UNIQUE,
    ADD COLUMN password varchar(64);

UPDATE users
SET login    = 'admin',
    password = 'admin'
WHERE id = 1;

ALTER TABLE users
    ALTER COLUMN login SET NOT NULL,
    ALTER COLUMN password SET NOT NULL;

CREATE TABLE IF NOT EXISTS user_role
(
    user_id bigint NOT NULL,
    roles   varchar(64)
);

