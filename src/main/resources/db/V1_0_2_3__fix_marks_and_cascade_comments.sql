ALTER TABLE comments
    DROP CONSTRAINT comments_recipe_id_fkey,
    DROP CONSTRAINT comments_user_id_fkey;

ALTER TABLE comments
    ADD CONSTRAINT comments_recipe_id_fkey
        FOREIGN KEY (recipe_id) REFERENCES recipes ON DELETE CASCADE,
    ADD CONSTRAINT comments_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE;


CREATE TABLE marks_new
(
    user_id   bigint REFERENCES users,
    recipe_id bigint REFERENCES recipes ON DELETE CASCADE,
    mark      smallint CHECK (mark > 0 AND mark <= 5),
    PRIMARY KEY (user_id, recipe_id)
);

DELETE
FROM marks
WHERE (user_id, recipe_id) IN (SELECT user_id, recipe_id FROM marks GROUP BY user_id, recipe_id HAVING COUNT(*) > 1);


INSERT INTO marks_new (user_id, recipe_id, mark)
SELECT user_id, recipe_id, mark
FROM marks;

DROP TABLE marks;

ALTER TABLE marks_new
    RENAME TO marks;

CREATE OR REPLACE TRIGGER add_avg_mark_trigger
    AFTER INSERT
    ON marks
    FOR EACH ROW
EXECUTE FUNCTION add_avg_mark();

CREATE OR REPLACE TRIGGER remove_avg_mark_trigger
    AFTER DELETE
    ON marks
    FOR EACH ROW
EXECUTE FUNCTION remove_avg_mark();

CREATE OR REPLACE TRIGGER update_avg_mark_trigger
    AFTER UPDATE
    ON marks
    FOR EACH ROW
EXECUTE FUNCTION update_avg_mark();