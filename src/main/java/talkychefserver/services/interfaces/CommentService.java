package talkychefserver.services.interfaces;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.CommentDto;
import talkychefserver.model.dto.IdDto;

import java.util.List;

public interface CommentService {
    ResponseEntity<IdDto> postComment(CommentDto commentDto);

    ResponseEntity<IdDto> updateComment(CommentDto commentDto);

    ResponseEntity<Void> deleteComment(Long commentId);

    ResponseEntity<List<CommentDto>> getRecipeComments(Long id);
}
