package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Markss;

@Repository
public interface MarksRepository extends CrudRepository<Markss, Long>{
}
