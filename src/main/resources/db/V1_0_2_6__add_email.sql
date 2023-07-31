ALTER TABLE user_info
ADD COLUMN email varchar(255) UNIQUE,
ADD COLUMN token varchar(255)
