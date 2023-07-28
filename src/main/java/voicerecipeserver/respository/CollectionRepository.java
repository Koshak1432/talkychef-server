package voicerecipeserver.respository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import voicerecipeserver.model.entities.Collection;
import voicerecipeserver.model.entities.Recipe;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends CrudRepository<Collection, Long> {
    @Query(value = """
                   SELECT * FROM collections
                    WHERE name ILIKE :namePart || '%'
                    UNION
                    SELECT * FROM collections
                    WHERE name ILIKE '% ' || :namePart || '%'
                    ORDER BY name
                    LIMIT CASE WHEN (:limit > 0) THEN :limit END
            """, nativeQuery = true)
    List<Collection> findByNameContaining(Long limit, String namePart);

    Optional<Collection> findByName(String name);

    @Modifying()
    @Query(value = """
            INSERT INTO collections_distribution(collection_id, recipe_id) VALUES (:collectionId, :recipeId);
            UPDATE collections
            SET number=number+1
            WHERE  id=:collectionId
            """, nativeQuery = true)
    void addRecipeToCollection(long recipeId, long collectionId);

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM collections_distribution
            WHERE  collection_id=:collectionId AND recipe_id =:recipeId
            """, nativeQuery = true)
    void deleteRecipeFromCollection(Long recipeId, Long collectionId);

    List<Collection> findByAuthorId(Long id);

    @Query(value = """
          SELECT * FROM collections 
          JOIN collections_distribution cd ON collections.id = cd.collection_id
          WHERE recipe_id =:recipeId AND collection_id=:collectionId
            """, nativeQuery = true)
    Optional<Collection> findRecipe(Long recipeId, Long collectionId);

}
