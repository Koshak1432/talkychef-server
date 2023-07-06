CREATE TABLE IF NOT EXISTS markss
(
    id        bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id   varchar references users(uid),
    recipe_id bigint references recipes,
    mark      smallint
);


