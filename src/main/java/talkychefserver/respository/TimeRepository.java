package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Category;
import voicerecipeserver.model.entities.Step;
import voicerecipeserver.model.entities.Time;

import java.util.List;

@Repository
public interface TimeRepository extends CrudRepository<Time, Long> {
    List<Time> findAll();

}
