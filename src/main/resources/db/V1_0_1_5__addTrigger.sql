CREATE TRIGGER trg_delete_media
    BEFORE DELETE ON recipes
    FOR EACH ROW
BEGIN
DELETE FROM media WHERE id = OLD.media_id AND value <> 172;
END;