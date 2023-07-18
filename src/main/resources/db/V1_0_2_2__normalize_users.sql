CREATE TABLE IF NOT EXISTS user_info
(
    user_id  bigint PRIMARY KEY REFERENCES users,
    image_id bigint UNIQUE REFERENCES media,
    info     varchar(1024),
    tg_link  varchar(256),
    vk_link  varchar(256)
);

ALTER TABLE users
    DROP COLUMN image_url,
    DROP COLUMN info,
    DROP COLUMN ok_link,
    DROP COLUMN vk_link,
    DROP COLUMN tg_link;
