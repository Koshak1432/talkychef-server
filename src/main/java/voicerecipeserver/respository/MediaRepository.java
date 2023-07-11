package voicerecipeserver.respository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import voicerecipeserver.model.entities.Media;

import java.util.List;

@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {
}
