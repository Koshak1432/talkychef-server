package talkychefserver.respositories;

import org.springframework.data.repository.CrudRepository;
import talkychefserver.model.entities.Selection;

import java.util.List;

public interface SelectionRepository extends CrudRepository<Selection, Long> {
    List<Selection> findAll();
}
