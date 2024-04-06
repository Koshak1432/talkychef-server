package talkychefserver.respositories;

import org.springframework.data.repository.CrudRepository;
import talkychefserver.model.entities.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> getCommentsByRecipeId(Long id);

}
