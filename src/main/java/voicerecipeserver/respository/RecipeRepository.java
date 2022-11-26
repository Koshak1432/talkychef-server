package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    //todo захардкодил цифру - нехорошо
    List<Recipe> findFirst10ByNameContainingIgnoreCase(String inline);
}
