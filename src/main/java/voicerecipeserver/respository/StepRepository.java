package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Step;

@Repository
public interface StepRepository extends CrudRepository<Step, Long> {
}
