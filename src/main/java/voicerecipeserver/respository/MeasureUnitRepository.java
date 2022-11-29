package voicerecipeserver.respository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Ingredient;
import voicerecipeserver.model.entities.MeasureUnit;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasureUnitRepository extends CrudRepository<MeasureUnit, Long> {

    Optional<MeasureUnit> findByName(String name);

    List<MeasureUnit> findFirst5ByNameContaining(String lowerCaseInfix);
}
