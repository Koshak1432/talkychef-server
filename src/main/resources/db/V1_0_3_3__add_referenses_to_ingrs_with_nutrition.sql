CREATE TABLE ingredient_mappings (
                                     ingredient_id BIGINT PRIMARY KEY REFERENCES ingredients,
                                     similar_ingredient_with_nutrition_id BIGINT REFERENCES ingredients_with_nutrition
);
create extension pg_trgm;

DO $$
    DECLARE
        ingredient RECORD;
        similar_ingredient RECORD;
    BEGIN
        FOR ingredient IN SELECT * FROM ingredients LOOP
                SELECT iwn.ingredient_id
                INTO similar_ingredient
                FROM ingredients_with_nutrition iwn
                ORDER BY similarity(ingredient.name, iwn.product_name) DESC
                LIMIT 1;

                INSERT INTO ingredient_mappings (ingredient_id, similar_ingredient_with_nutrition_id)
                VALUES (ingredient.id, similar_ingredient.ingredient_id);
            END LOOP;
    END;
$$ LANGUAGE plpgsql;

