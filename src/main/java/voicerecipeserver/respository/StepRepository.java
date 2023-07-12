package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Step;

import java.util.Optional;

@Repository
public interface StepRepository extends CrudRepository<Step, Long> {
    Optional<Step> findStepByMediaId(Long mediaId);
}
