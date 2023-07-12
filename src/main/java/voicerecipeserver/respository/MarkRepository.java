package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Mark;

@Repository
public interface MarkRepository extends CrudRepository<Mark, Long>{
}
