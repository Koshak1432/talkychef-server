package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import voicerecipeserver.model.entities.AvgMark;

public interface AvgMarkRepository  extends CrudRepository<AvgMark, Long>  {
}
