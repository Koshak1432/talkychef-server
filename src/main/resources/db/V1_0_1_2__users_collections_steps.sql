ALTER TABLE collections
    ADD COLUMN number integer default (0) NOT NULL;

AlTER TABLE recipe_steps
    ALTER COLUMN media_id DROP NOT NULL;

ALTER TABLE users
    RENAME COLUMN username TO uid;

-- почему-то h2db не разрешает несколько команд в одном alter
ALTER TABLE users
    ADD COLUMN display_name varchar(64) default ('Real Chief') NOT NULL;
ALTER TABLE users
    ADD COLUMN image_url varchar(256);
ALTER TABLE users
    ADD COLUMN info varchar(1024);
ALTER TABLE users
    ADD COLUMN ok_link varchar(256);
ALTER TABLE users
    ADD COLUMN tg_link varchar(256);
ALTER TABLE users
    ADD COLUMN vk_link varchar(256);


UPDATE collections
SET number = (SELECT COUNT(recipe_id)
              FROM collections_distribution
              WHERE collection_id = collections.id)
