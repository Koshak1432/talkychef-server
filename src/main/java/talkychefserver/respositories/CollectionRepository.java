package talkychefserver.respositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import talkychefserver.model.entities.Collection;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends CrudRepository<Collection, Long> {
    @Query(value = """
                (
                    SELECT * FROM collections
                    WHERE name ILIKE :namePart || '%'
                    ORDER BY name
                )
                UNION
                (
                    SELECT * FROM collections
                    WHERE name ILIKE '% ' || :namePart || '%'
                    ORDER BY name
                )
                LIMIT :limit OFFSET :limit * :page
            """, nativeQuery = true)
    List<Collection> findByNameContaining(String namePart, int limit, int page);

    Optional<Collection> findCollectionByName(String name);

    @Modifying
    @Query(value = """
            INSERT INTO collections_distribution(collection_id, recipe_id) VALUES (:collectionId, :recipeId);
            UPDATE collections
            SET number = number + 1
            WHERE id = :collectionId
            """, nativeQuery = true)
    void addRecipeToCollection(long recipeId, long collectionId);

    @Modifying
    @Query(value = """
            DELETE FROM collections_distribution
            WHERE  (collection_id = :collectionId AND recipe_id = :recipeId);
            UPDATE collections
            SET number = number - 1
            WHERE id = :collectionId
            """, nativeQuery = true)
    void deleteRecipeFromCollection(Long recipeId, Long collectionId);


    @Query(value = """
                SELECT * FROM collections
                WHERE author_id = :id
                LIMIT :limit OFFSET :limit * :page
            """, nativeQuery = true)
    List<Collection> findByAuthorIdWithOffset(Long id, int limit, int page);

    @Query(value = """
            SELECT * FROM collections
            JOIN collections_distribution cd ON collections.id = cd.collection_id
            WHERE recipe_id = :recipeId AND collection_id = :collectionId
              """, nativeQuery = true)
    Optional<Collection> findRecipeInCollection(Long recipeId, Long collectionId);

    @Query(value = """
            SELECT * FROM collections
            WHERE author_id = :id AND name = :name
              """, nativeQuery = true)
    Optional<Collection> findByAuthorIdUserRecipeCollection(Long id, String name);
}
