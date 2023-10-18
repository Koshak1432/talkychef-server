CREATE TABLE IF NOT EXISTS selections
(
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS selections_distribution
(
    selection_id bigint NOT NULL REFERENCES selections,
    category_id bigint NOT NULL REFERENCES categories,
    PRIMARY KEY (selection_id, category_id)
);