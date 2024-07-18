ALTER TABLE recipes
    ALTER COLUMN proteins TYPE numeric,
    ALTER COLUMN fats TYPE numeric,
    ALTER COLUMN carbohydrates TYPE numeric,
    ALTER COLUMN kilocalories TYPE numeric;



CREATE OR REPLACE FUNCTION calculate_dish_nutrition(recipee_id BIGINT)
    RETURNS VOID AS
$$
DECLARE
    total_calories      NUMERIC(10,2) := 1;
    total_protein       NUMERIC(10,2) := 1;
    total_fat           NUMERIC(10,2) := 1;
    total_carbohydrates NUMERIC(10,2) := 1;
    total_weights       NUMERIC(10,2) := 1;
    ingredient_weight   NUMERIC(10,2);
    ingredient_record   RECORD;
    serve            INT;
BEGIN
    SELECT COALESCE(servings, 1)
    INTO serve
    FROM recipes
    WHERE id = recipee_id;

    FOR ingredient_record IN
        SELECT id.*,
               p.energy_value,
               p.protein,
               p.fat,
               p.carbohydrates,
               mu.conversion_to_grams
        FROM ingredients_distribution id
            JOIN ingredient_mappings im ON id.ingredient_id = im.ingredient_id
                 JOIN ingredients_with_nutrition p
                      ON im.similar_ingredient_with_nutrition_id = p.ingredient_id
                 JOIN measure_units mu ON id.measure_unit_id = mu.id
        WHERE id.recipe_id = recipee_id
        LOOP
            ingredient_weight := ingredient_record.measure_unit_count *
                                 ingredient_record.conversion_to_grams;
            total_weights := total_weights + ingredient_weight;
            total_calories := total_calories + (ingredient_weight / 100) *
                                               ingredient_record.energy_value;
            total_protein := total_protein + (ingredient_weight / 100) *
                                             ingredient_record.protein;
            total_fat := total_fat +
                         (ingredient_weight / 100) * ingredient_record.fat;
            total_carbohydrates := total_carbohydrates +
                                   (ingredient_weight / 100) *
                                   ingredient_record.carbohydrates;
        END LOOP;
    IF serve IS NOT NULL AND serve > 0 THEN
        total_calories := total_calories / serve;
        total_protein := total_protein / serve;
        total_fat := total_fat / serve;
        total_carbohydrates := total_carbohydrates / serve;
    END IF;
    UPDATE recipes
    SET kilocalories  = total_calories,
        proteins      = total_protein,
        fats          = total_fat,
        carbohydrates = total_carbohydrates,
        total_weight  = total_weights
    WHERE recipes.id = recipee_id;
END;
$$ LANGUAGE plpgsql;


DO
$$
    DECLARE
        recipe RECORD;
    BEGIN
        FOR recipe IN
            SELECT id FROM recipes
            LOOP
                PERFORM calculate_dish_nutrition(recipe.id);
            END LOOP;
    END;
$$;
