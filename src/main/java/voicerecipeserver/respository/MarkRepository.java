package voicerecipeserver.respository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Mark;
import voicerecipeserver.model.entities.MarkKey;

import java.util.Optional;

@Repository
public interface MarkRepository extends CrudRepository<Mark, MarkKey>{
    @Query(value = """
                    SELECT * FROM marks m WHERE m.user_id = :userId AND m.recipe_id = :recipeId
            """, nativeQuery = true)
    Optional<Mark> findByUserIdAndRecipeId(@Param("userId") Long userId, @Param("recipeId") Long recipeId);
    @Modifying
    @Transactional
    @Query(value = """
                    DELETE FROM marks m WHERE m.user_id = :userId AND m.recipe_id = :recipeId
            """, nativeQuery = true)
    void deleteByIdUserIdAndIdRecipeId(@Param("userId") Long userId, @Param("recipeId") Long recipeId);

}
