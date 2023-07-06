package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Mark;

@Repository
public interface MarksRepository extends CrudRepository<Mark, Long>{
}
