ALTER TABLE collections
    ADD COLUMN author_id bigint,
    DROP CONSTRAINT collections_name_key;

CREATE OR REPLACE FUNCTION delete_collections_distribution()
    RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM collections_distribution
    WHERE collection_id = OLD.id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_delete_collections_distribution
    AFTER DELETE ON collections
    FOR EACH ROW
EXECUTE FUNCTION delete_collections_distribution();