ALTER TABLE users
    ADD COLUMN password varchar(64);

UPDATE users
SET password = 'admin'
WHERE id = 1;

ALTER TABLE users
    ALTER COLUMN uid SET NOT NULL,
    ALTER COLUMN password SET NOT NULL;

CREATE TABLE IF NOT EXISTS roles
(
    id   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id bigint REFERENCES users ON DELETE CASCADE,
    role_id bigint REFERENCES roles ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO roles(name)
VALUES ('ADMIN');

INSERT INTO roles(name)
VALUES ('USER');

INSERT INTO user_role(user_id, role_id)
VALUES (1, 1);
