ALTER TABLE users
    ADD COLUMN password varchar(64);

UPDATE users
SET uid    = 'admin',
    password = 'admin'
WHERE id = 1;

ALTER TABLE users
    ALTER COLUMN uid SET NOT NULL,
    ALTER COLUMN password SET NOT NULL;

CREATE TABLE IF NOT EXISTS roles
(
    id bigint GENERATED ALWAYS AS IDENTITY,
    name varchar(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id bigint REFERENCES users,
    role_id bigint REFERENCES roles
);


