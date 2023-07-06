CREATE TABLE IF NOT EXISTS marks
(
    id        bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id  bigint references users(id),
    recipe_id bigint references recipes,
    mark      smallint
);
--
-- insert into marks(user_uid, recipe_id, mark) VALUES ((select id from users limit 1),1,2);
--
-- select * from users;
-- delete from flyway_schema_history where installed_rank = 4