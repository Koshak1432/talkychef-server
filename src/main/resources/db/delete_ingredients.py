import psycopg2
from sshtunnel import SSHTunnelForwarder
import time


def update_similar_ingredients():
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

            # Execute query to fetch data for update
            cursor.execute("""
                WITH matched_pairs AS (
                    SELECT r1.id AS ing_id,
                           r2.id AS sim_ing_id,
                           similarity(r1.name, r2.name) AS similarity_score,
                           ROW_NUMBER() OVER (PARTITION BY r1.id ORDER BY similarity(r1.name, r2.name) DESC) AS rn
                    FROM ingredients r1
                    CROSS JOIN ingredients r2
                    WHERE r1.id < r2.id
                    AND similarity(r1.name, r2.name) > 0.5
                )
                SELECT DISTINCT ON (idist.ingredient_id)
                       idist.ingredient_id AS id,
                       matches.ing_id,
                       matches.similarity_score
                FROM ingredients_distribution idist
                JOIN (
                    SELECT ing_id, sim_ing_id, similarity_score
                    FROM matched_pairs
                    WHERE rn = 1
                    ORDER BY similarity_score DESC
                ) AS matches
                ON idist.ingredient_id = matches.sim_ing_id
                ORDER BY idist.ingredient_id, similarity_score DESC
            """)

            print("Fetching records for update...")

            records = cursor.fetchall()

            for rec in records:
                try:
                    # Perform update
                    cursor.execute("""
                        UPDATE ingredients_distribution
                        SET ingredient_id = %s
                        WHERE ingredient_id = %s
                    """, (rec[1], rec[0]))

                    print(f"Updated ingredient_id {rec[0]} to {rec[1]}")

                    # Commit the transaction
                    connection.commit()

                except psycopg2.IntegrityError as e:
                    # Handle unique key violation error
                    connection.rollback()
                    cursor.execute("""
                        DELETE FROM ingredients_distribution
                        WHERE recipe_id IN (
                            SELECT recipe_id
                            FROM ingredients_distribution
                            WHERE ingredient_id = %s
                            INTERSECT
                            SELECT recipe_id
                            FROM ingredients_distribution
                            WHERE ingredient_id = %s
                        ) AND ingredient_id = %s;
                    """, (rec[1], rec[0], rec[0]))
                    connection.commit()  # Commit the DELETE operation

                    cursor.execute("""
                            SELECT name
                            FROM ingredients
                            WHERE id IN (%s, %s);
                        """, (rec[1], rec[0]))
                    tmp = cursor.fetchall()

                    print("Deleted row with: ", tmp, rec[1], rec[0])

    except Exception as error:
        print(f"Error while connecting to PostgreSQL: {error}")

    finally:
        # Close cursor and connection
        if connection:
            cursor.close()
            connection.close()

        end_time = time.time()
        runtime = end_time - start_time
        print(f"Program runtime: {runtime} seconds")


if __name__ == "__main__":
    update_similar_ingredients()
