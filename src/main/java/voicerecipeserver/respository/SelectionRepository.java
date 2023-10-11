package voicerecipeserver.respository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import voicerecipeserver.model.entities.Selection;

import java.util.List;

public interface SelectionRepository extends CrudRepository<Selection, Long> {
    List<Selection> findAll();
}
