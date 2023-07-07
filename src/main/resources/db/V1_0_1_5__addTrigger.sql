CREATE OR REPLACE FUNCTION delete_unused_media()
    RETURNS TRIGGER AS
$$
BEGIN
    DELETE
    FROM media
    WHERE id = OLD.media_id
      AND id <> 172;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE TRIGGER add_avg_mark_trigger
    BEFORE DELETE
    ON recipes
    FOR EACH ROW
EXECUTE FUNCTION delete_unused_media();