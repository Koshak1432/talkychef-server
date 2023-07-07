package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import voicerecipeserver.model.entities.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
