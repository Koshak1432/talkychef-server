ALTER TABLE collections
    ADD COLUMN author_id bigint,
    DROP CONSTRAINT collections_name_key;
