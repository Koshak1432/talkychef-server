package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Ingredient;

import java.util.List;
import java.util.Optional;


@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    Optional<Ingredient> findByName(String name);

    //todo захардкодил цифру - нехорошо
    List<Ingredient> findFirst3ByNameContainingIgnoreCase(String infix);

}
