CREATE TABLE IF NOT EXISTS measure_units
(
  id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name varchar(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS ingredients
(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS media_types
(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    mime_type varchar(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS media
(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type_id bigint NOT NULL REFERENCES media_types,
    file_data bytea NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username varchar(64) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS recipes
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

CREATE TABLE IF NOT EXISTS ingredients_distribution
(
    recipe_id bigint NOT NULL REFERENCES recipes ON DELETE CASCADE ,
    ingredient_id bigint NOT NULL REFERENCES ingredients,
    measure_unit_id bigint NOT NULL REFERENCES measure_units,
    measure_unit_count float NOT NULL,
    PRIMARY KEY(recipe_id, ingredient_id),
    CHECK ( measure_unit_count > 0 )
);

CREATE TABLE IF NOT EXISTS recipe_steps(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    media_id bigint NOT NULL REFERENCES media,
    description text NOT NULL,
    wait_time_mins integer,
    step_num integer NOT NULL,
    recipe_id bigint NOT NULL REFERENCES recipes ON DELETE CASCADE,
    CHECK (
        ((wait_time_mins IS NULL) OR ((wait_time_mins IS NOT NULL) AND (wait_time_mins > 0)))
        AND step_num >= 0
          ),
    UNIQUE (step_num, recipe_id)
);

CREATE TABLE IF NOT EXISTS categories(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories_distribution(
    category_id bigint NOT NULL REFERENCES categories ON DELETE CASCADE,
    recipe_id bigint NOT NULL REFERENCES recipes ON DELETE CASCADE,
    PRIMARY KEY (category_id, recipe_id)
);

CREATE TABLE IF NOT EXISTS collections(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS collections_distribution(
    collection_id bigint NOT NULL REFERENCES collections ON DELETE CASCADE,
    recipe_id bigint NOT NULL REFERENCES recipes ON DELETE CASCADE,
    PRIMARY KEY (collection_id, recipe_id)
);