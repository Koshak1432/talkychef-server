ALTER TABLE user_info ADD COLUMN display_name varchar(32);

UPDATE user_info
SET display_name = (SELECT display_name FROM users WHERE users.id = user_info.user_id);

ALTER TABLE users DROP COLUMN display_name;
