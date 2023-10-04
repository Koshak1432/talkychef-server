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
                    (
                        SELECT * FROM recipes
                        WHERE name ILIKE :namePart || '%'
                        ORDER BY name
                    )
                    UNION
                    (
                        SELECT * FROM recipes
                        WHERE name ILIKE '% ' || :namePart || '%'
                        ORDER BY name
                    )
                    LIMIT :limit
            """, nativeQuery = true)
    List<Recipe> findByNameContaining(@Param("namePart") String inline, @Param("limit") Integer limit);

    // https://medium.com/swlh/sql-pagination-you-are-probably-doing-it-wrong-d0f2719cc166 - performance issue
    // вообще хотелось бы этот метод в репозиторий коллекций добавить, но там проблема с конвертацией.
    @Query(value = """
                    SELECT recipes.* FROM recipes
                    JOIN collections_distribution distr ON recipes.id = distr.recipe_id
                    WHERE distr.collection_id = :collectionId
                    ORDER BY distr.recipe_id
                    LIMIT :numRecipes OFFSET :offset
            """, nativeQuery = true)
    List<Recipe> findRecipesWithOffsetFromCollectionById(int numRecipes, int offset, long collectionId);


    Optional<Recipe> findRecipeByMediaId(Long mediaId);


    @Query(value = """
                    SELECT recipes.* FROM recipes
                    LEFT JOIN avg_marks ON recipes.id = avg_marks.recipe_id
                    WHERE avg_marks.recipe_id IS NULL
                    ORDER BY random()
                    LIMIT :limit
            """, nativeQuery = true)
    List<Recipe> findRandomWithLimit(int limit);


    @Query(value = """
                   SELECT recipes.*
                   FROM recipes
                   JOIN avg_marks ON recipes.id = avg_marks.recipe_id
                   ORDER BY avg_mark DESC
                   LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<Recipe> findTopRecipesWithLimitAndOffset(int limit, int offset);

    @Query(value = """
                   SELECT recipes.*
                   FROM recipes
                   WHERE id in :ids
            """, nativeQuery = true)
    List<Recipe> findByIds(@Param("ids") List<Long> recipesIds);
}
