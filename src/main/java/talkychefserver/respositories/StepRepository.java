package talkychefserver.respositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import talkychefserver.model.entities.Step;

import java.util.Optional;

@Repository
public interface StepRepository extends CrudRepository<Step, Long> {
    Optional<Step> findStepByMediaId(Long mediaId);
}
