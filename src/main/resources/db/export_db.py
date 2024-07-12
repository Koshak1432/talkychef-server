import time
import psycopg2
import csv
from sshtunnel import SSHTunnelForwarder

recipe_ids = [248, 249, 250]
def import_db():
    try:
        # SSH parameters
        ssh_host = '185.128.106.56'
        ssh_port = 22
        ssh_username = ''
        ssh_password = ''

        # PostgreSQL parameters
        pg_host = 'localhost'
        pg_port = 5432
        pg_user = ''
        pg_password = ''
        pg_dbname = ''

        start_time = time.time()

        # Establish SSH tunnel to PostgreSQL server
        with SSHTunnelForwarder(
                (ssh_host, ssh_port),
                ssh_username=ssh_username,
                ssh_password=ssh_password,
                remote_bind_address=(pg_host, pg_port)
        ) as tunnel:
            # Connect to PostgreSQL via the SSH tunnel
            connection = psycopg2.connect(
                dbname=pg_dbname,
                user=pg_user,
                password=pg_password,
                host="localhost",
                port=tunnel.local_bind_port
            )

            cursor = connection.cursor()

            # Экспорт ингредиентов для указанных рецептов
            cursor.execute("""
                SELECT DISTINCT i.id, i.name
                FROM ingredients i
                JOIN ingredients_distribution id ON i.id = id.ingredient_id
                WHERE id.recipe_id IN %s;
            """, (tuple(recipe_ids),))
            ingredients = cursor.fetchall()

            # Экспорт распределения ингредиентов для указанных рецептов
            cursor.execute("""
                SELECT recipe_id, ingredient_id, measure_unit_id, measure_unit_count
                FROM ingredients_distribution
                WHERE recipe_id IN %s;
            """, (tuple(recipe_ids),))
            ingredients_distribution = cursor.fetchall()

            # Экспорт пользователей, которые являются авторами указанных рецептов
            cursor.execute("""
                SELECT DISTINCT u.id, u.uid, u.password
                FROM users u
                JOIN recipes r ON u.id = r.author_id
                WHERE r.id IN %s;
            """, (tuple(recipe_ids),))
            users = cursor.fetchall()

            # Экспорт указанных рецептов
            cursor.execute("""
                SELECT id, name, media_id, author_id, cook_time_mins, prep_time_mins, kilocalories, proteins, fats, carbohydrates, servings, total_weight
                FROM recipes
                WHERE id IN %s;
            """, (tuple(recipe_ids),))
            recipes = cursor.fetchall()

            # Экспорт распределения категорий для указанных рецептов
            cursor.execute("""
                SELECT category_id, recipe_id
                FROM categories_distribution
                WHERE recipe_id IN %s;
            """, (tuple(recipe_ids),))
            categories_distribution = cursor.fetchall()

            # Экспорт единиц измерения
            cursor.execute("""
                SELECT id, name, conversion_to_grams
                FROM measure_units;
            """)
            measure_units = cursor.fetchall()

            # Сохранение данных в CSV файлы
            with open('ingredients.csv', 'w', newline='') as f:
                writer = csv.writer(f)
                writer.writerows(ingredients)

            with open('ingredients_distribution.csv', 'w', newline='') as f:
                writer = csv.writer(f)
                writer.writerows(ingredients_distribution)

            with open('users.csv', 'w', newline='') as f:
                writer = csv.writer(f)
                writer.writerows(users)

            with open('recipes.csv', 'w', newline='') as f:
                writer = csv.writer(f)
                writer.writerows(recipes)

            with open('categories_distribution.csv', 'w', newline='') as f:
                writer = csv.writer(f)
                writer.writerows(categories_distribution)

            with open('measure_units.csv', 'w', newline='') as f:
                writer = csv.writer(f)
                writer.writerows(measure_units)

            cursor.close()
            connection.close()

    except Exception as e:
        print(f"Error: {e}")

    end_time = time.time()
    runtime = end_time - start_time
    print(f"Program runtime: {runtime} seconds")

if __name__ == "__main__":
    import_db()
