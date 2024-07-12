CREATE TABLE IF NOT EXISTS ingredients_with_nutrition
(
    ingredient_id            BIGINT PRIMARY KEY,
    product_name          VARCHAR(64) NOT NULL UNIQUE,
    daily_dose            BIGINT,
    water                 NUMERIC,
    protein               NUMERIC,
    fat                   NUMERIC,
    sfa                   NUMERIC,
    cholesterol           NUMERIC,
    pufa                  NUMERIC,
    starch                NUMERIC,
    carbohydrates         NUMERIC,
    dietary_fiber         NUMERIC,
    organic_acids         NUMERIC,
    ash                   NUMERIC,
    sodium                NUMERIC,
    potassium             NUMERIC,
    calcium               NUMERIC,
    magnesium             NUMERIC,
    phosphorus            NUMERIC,
    iron                  NUMERIC,
    retinol               NUMERIC,
    carotene              NUMERIC,
    retinol_equivalent    NUMERIC,
    tocopherol_equivalent NUMERIC,
    thiamine              NUMERIC,
    riboflavin            NUMERIC,
    niacin                NUMERIC,
    niacin_equivalent     NUMERIC,
    ascorbic_acid         NUMERIC,
    energy_value          NUMERIC,
    serving               NUMERIC,
    categories            VARCHAR     NOT NULL,
    subcategories         VARCHAR,
    subcategories2        VARCHAR,
    subcategories3        VARCHAR,
    subcategories4        VARCHAR
);


COPY ingredients_with_nutrition (ingredient_id, product_name, daily_dose, water, protein, fat, sfa, cholesterol, pufa, starch, carbohydrates,
               dietary_fiber, organic_acids, ash, sodium, potassium, calcium, magnesium, phosphorus, iron, retinol,
               carotene, retinol_equivalent, tocopherol_equivalent, thiamine, riboflavin, niacin, niacin_equivalent,
               ascorbic_acid, energy_value, serving, categories, subcategories, subcategories2, subcategories3,
               subcategories4)
    FROM '/src/main/resources/data/NutriMind_products.csv' DELIMITER ',' CSV HEADER;

ALTER TABLE measure_units ADD COLUMN conversion_to_grams BIGINT;
UPDATE measure_units SET conversion_to_grams = 1 WHERE name = 'граммов';
UPDATE measure_units SET conversion_to_grams = 1 WHERE name = 'миллилитров';
UPDATE measure_units SET conversion_to_grams = 5 WHERE name = 'ч.л.';
UPDATE measure_units SET conversion_to_grams = 15 WHERE name = 'ст.л.';

INSERT INTO measure_units(name, conversion_to_grams)  VALUES ('единиц', 100);


ALTER  TABLE recipes ADD COLUMN total_weight BIGINT;