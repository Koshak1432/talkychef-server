package talkychefserver.respositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import talkychefserver.model.entities.Media;

@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {}
