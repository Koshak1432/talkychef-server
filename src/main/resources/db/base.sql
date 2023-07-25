DROP PROCEDURE IF EXISTS importTable;
CREATE OR REPLACE PROCEDURE importTable(
    table_name varchar(255),
    path_to_file varchar(255),
    deli nchar(1)
)
    LANGUAGE plpgsql AS
$$
BEGIN
    EXECUTE FORMAT('COPY %I FROM %L DELIMITER %L CSV HEADER', table_name, path_to_file, deli);
END;
$$;


DROP PROCEDURE IF EXISTS exportTable;
CREATE OR REPLACE PROCEDURE exportTable(
    table_name varchar(255),
    name_file varchar(255),
    deli nchar(1)
)
    LANGUAGE plpgsql AS
$$
BEGIN
    EXECUTE FORMAT('COPY %I TO %L DELIMITER %L CSV HEADER', table_name, name_file, deli);
END;
$$;


CALL exportTable('recipes', '/home/alina/sber/src/main/resources/db/recipes.csv', ',');

call importTable('recipes2', '/home/alina/sber/src/main/resources/db/recipes.csv', ',');


CREATE TABLE IF NOT EXISTS recipes2
(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(128) NOT NULL,
    media_id bigint NOT NULL REFERENCES media,
    author_id bigint NOT NULL REFERENCES users,
    cook_time_mins integer NOT NULL ,
    prep_time_mins integer,
    kilocalories integer,
    proteins integer,
    fats integer,
    carbohydrates integer
        CHECK (
                (cook_time_mins > 0)
                AND ((prep_time_mins IS NULL) OR ((prep_time_mins IS NOT NULL) AND (prep_time_mins > 0)))
                AND ((kilocalories IS NULL) OR ((kilocalories IS NOT NULL) AND (kilocalories > 0)))
                AND ((proteins IS NULL) OR ((proteins IS NOT NULL) AND (proteins > 0)))
                AND ((fats IS NULL) OR ((fats IS NOT NULL) AND (fats > 0)))
                AND ((carbohydrates IS NULL) OR ((carbohydrates IS NOT NULL) AND (carbohydrates > 0)))
            )
);

DO $$
    DECLARE
        file_content bytea;
        file_path text;
        i integer := 1;
    BEGIN
        WHILE i <= 1000 LOOP
                -- Replace '/path/to/example.txt' with the actual path to your file for each iteration
                file_path := '/home/alina/Downloads/chicken.jpeg';

                -- Read the file contents using pg_read_binary_file() function
                file_content := pg_read_binary_file(file_path);

                -- Insert the file contents into the table
                INSERT INTO media (type_id, file_data) VALUES (1, file_content);

                i := i + 1;
            END LOOP;
    END $$;

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