CREATE TABLE IF NOT EXISTS marks
(
    id        bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id   bigint references users,
    recipe_id bigint references recipes,
    mark      smallserial
);
