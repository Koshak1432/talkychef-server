ALTER TABLE recipes
    ADD CONSTRAINT recipe_media_unique UNIQUE (media_id);

ALTER TABLE recipe_steps
    ADD CONSTRAINT step_media_unique UNIQUE (media_id);