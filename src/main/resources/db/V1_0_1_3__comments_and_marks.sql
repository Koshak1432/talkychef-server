CREATE TABLE IF NOT EXISTS comments
(
    id        bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id   bigint REFERENCES users,
    recipe_id bigint REFERENCES recipes,
    date      timestamp,
    content   text NOT NULL CHECK (trim(content) <> '')
);

-- надо убрать искусственный pk и сделать составной
CREATE TABLE IF NOT EXISTS marks
(
    id        bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id   bigint REFERENCES users,
    recipe_id bigint REFERENCES recipes ON DELETE CASCADE,
    mark      smallint CHECK (mark > 0 AND mark <= 5)
);

CREATE TABLE IF NOT EXISTS avg_marks
(
    recipe_id bigint REFERENCES recipes PRIMARY KEY,
    avg_mark  real CHECK (avg_mark > 0),
    quantity  bigint CHECK (quantity >= 0)
);

CREATE OR REPLACE FUNCTION add_avg_mark()
    RETURNS TRIGGER AS
$$
BEGIN
    IF EXISTS (SELECT 1 FROM avg_marks WHERE recipe_id = NEW.recipe_id) THEN
        UPDATE avg_marks
        SET avg_mark = (SELECT AVG(mark) FROM marks WHERE recipe_id = NEW.recipe_id),
            quantity = quantity + 1
        WHERE recipe_id = NEW.recipe_id;
    ELSE
        INSERT INTO avg_marks (recipe_id, avg_mark, quantity)
        VALUES (NEW.recipe_id, NEW.mark, 1);
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION update_avg_mark()
    RETURNS TRIGGER AS
$$
BEGIN
    IF EXISTS (SELECT 1 FROM avg_marks WHERE recipe_id = NEW.recipe_id) THEN
        UPDATE avg_marks
        SET avg_mark = (SELECT AVG(mark) FROM marks WHERE recipe_id = NEW.recipe_id)
        WHERE recipe_id = NEW.recipe_id;
    ELSE
        INSERT INTO avg_marks (recipe_id, avg_mark, quantity)
        VALUES (NEW.recipe_id, NEW.mark, 1);
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION remove_avg_mark()
    RETURNS TRIGGER AS
$$
BEGIN
    UPDATE avg_marks
    SET avg_mark = (SELECT AVG(mark) FROM marks WHERE recipe_id = OLD.recipe_id),
        quantity = quantity - 1
    WHERE recipe_id = OLD.recipe_id;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;



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

