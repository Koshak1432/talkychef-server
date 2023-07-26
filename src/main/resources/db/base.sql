

 select REGEXP_REPLACE(name, '[а-яА-ЯёЁ,.!?;:()"\ ]', '', 'g') <> '' from recipes;

CREATE OR REPLACE FUNCTION check_and_delete_ingredient()
    RETURNS TRIGGER AS
$$
BEGIN
    DELETE FROM ingredients
    WHERE id = OLD.ingredient_id
      AND NOT EXISTS (
            SELECT 1 FROM ingredients_distribution
            WHERE ingredient_id = OLD.ingredient_id
        );
    RETURN OLD;
END;
$$
    LANGUAGE plpgsql;

CREATE TRIGGER after_delete_ingredients_distribution
    AFTER DELETE ON ingredients_distribution
    FOR EACH ROW
EXECUTE FUNCTION check_and_delete_ingredient();


CREATE OR REPLACE FUNCTION check_and_delete_media()
    RETURNS TRIGGER AS
$$
BEGIN
    DELETE FROM media
    WHERE id = OLD.media_id;
    RETURN OLD;
END;
$$
    LANGUAGE plpgsql;

CREATE  OR REPLACE TRIGGER after_delete_recipes
    AFTER DELETE ON recipes
    FOR EACH ROW
EXECUTE FUNCTION check_and_delete_media();

CREATE  OR REPLACE TRIGGER after_delete_steps
    AFTER DELETE ON recipe_steps
    FOR EACH ROW
EXECUTE FUNCTION check_and_delete_media();


 DELETE FROM avg_marks
 WHERE recipe_id IN (
     SELECT id FROM recipes WHERE REGEXP_REPLACE(name, '[а-яА-ЯёЁ,.!?;:()"\ ]', '', 'g') <> ''
 );

 DELETE FROM ingredients_distribution
 WHERE recipe_id IN (
     SELECT id FROM recipes WHERE REGEXP_REPLACE(name, '[а-яА-ЯёЁ,.!?;:()"\ ]', '', 'g') <> ''
 );

 DELETE FROM recipes
 WHERE REGEXP_REPLACE(name, '[а-яА-ЯёЁ,.!?;:()"\ ]', '', 'g') <> '';

