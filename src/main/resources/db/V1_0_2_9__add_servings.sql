ALTER TABLE recipes
    ADD COLUMN servings smallint CHECK (servings > 0)