package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import voicerecipeserver.model.entities.Collection;

import java.util.Optional;

public interface CollectionRepository extends CrudRepository<Collection, Long> {
    Optional<Collection> findByName(String name);
}
