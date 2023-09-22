CREATE TABLE IF NOT EXISTS categories
(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(32) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS categories_distribution
(
    category_id bigint references categories,
    recipe_id bigint references recipes,
    PRIMARY KEY (category_id, recipe_id)
);
