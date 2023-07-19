CREATE TABLE marks_new (
                           user_id   bigint REFERENCES users,
                           recipe_id bigint REFERENCES recipes ON DELETE CASCADE,
                           mark      smallint CHECK (mark > 0 AND mark <= 5),
                           PRIMARY KEY (user_id, recipe_id)
);

DELETE FROM marks WHERE (user_id, recipe_id) IN (SELECT user_id, recipe_id FROM marks GROUP BY user_id, recipe_id HAVING COUNT(*) > 1);


INSERT INTO marks_new (user_id, recipe_id, mark)
SELECT user_id, recipe_id, mark FROM marks;

DROP TABLE marks;

ALTER TABLE marks_new RENAME TO marks;
