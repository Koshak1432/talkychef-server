package talkychefserver.respository;

import org.springframework.data.repository.CrudRepository;
import talkychefserver.model.entities.AvgMark;

public interface AvgMarkRepository extends CrudRepository<AvgMark, Long> {}
