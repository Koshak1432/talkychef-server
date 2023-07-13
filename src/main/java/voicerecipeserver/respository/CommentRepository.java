package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import voicerecipeserver.model.entities.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> getCommentsByRecipeId(Long id);

}
