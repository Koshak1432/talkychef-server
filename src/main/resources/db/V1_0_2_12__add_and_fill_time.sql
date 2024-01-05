CREATE TABLE IF NOT EXISTS time
(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    time varchar(32) NOT NULL UNIQUE
);

INSERT INTO time (time)
VALUES ('до 10 минут'),
       ('10-20 минут'),
       ('20-30 минут'),
       ('30-60 минут'),
       ('Более часа');