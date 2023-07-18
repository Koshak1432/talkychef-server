package voicerecipeserver.respository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Recipe;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    @Query(value = """
                    SELECT * FROM recipes
                    WHERE name ILIKE :namePart || '%'
                    UNION
                    SELECT * FROM recipes
                    WHERE name ILIKE '% ' || :namePart || '%'
                    ORDER BY name
                    LIMIT CASE WHEN (:limit > 0) THEN :limit END
            """, nativeQuery = true)
    List<Recipe> findByNameContaining(@Param("namePart") String inline, @Param("limit") Integer limit);

    // https://medium.com/swlh/sql-pagination-you-are-probably-doing-it-wrong-d0f2719cc166 - performance issue
    // вообще хотелось бы этот метод в репозиторий коллекций добавить, но там проблема с конвертацией.
    @Query(value = """
                    SELECT * FROM recipes
                    WHERE id IN
                    (
                        SELECT recipe_id FROM collections_distribution
                        WHERE collection_id=:collectionId
                        ORDER BY recipe_id
                        LIMIT :numRecipes OFFSET :offset
            )
            """, nativeQuery = true)
    List<Recipe> findRecipesWithOffsetFromCollectionById(int numRecipes, int offset, long collectionId);


    Optional<Recipe> findRecipeByMediaId(Long mediaId);


}
