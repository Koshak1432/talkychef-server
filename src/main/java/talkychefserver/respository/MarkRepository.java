package talkychefserver.respository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import talkychefserver.model.entities.Mark;
import talkychefserver.model.entities.MarkKey;

import java.util.Optional;

@Repository
public interface MarkRepository extends CrudRepository<Mark, MarkKey> {
    @Query(value = """
                    SELECT * FROM marks m WHERE m.user_id = :userId AND m.recipe_id = :recipeId
            """, nativeQuery = true)
    Optional<Mark> findByUserIdAndRecipeId(Long userId, Long recipeId);

    @Modifying
    @Query(value = """
                    DELETE FROM marks m WHERE m.user_id = :userId AND m.recipe_id = :recipeId
            """, nativeQuery = true)
    void deleteByIdUserIdAndIdRecipeId(Long userId, Long recipeId);

}
