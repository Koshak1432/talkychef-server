package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.CommentDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

public interface CommentService {
    ResponseEntity<IdDto> postComment(CommentDto commentDto) throws NotFoundException;

    ResponseEntity<IdDto> updateComment(CommentDto commentDto) throws NotFoundException;

    ResponseEntity<Void> deleteComment(Long commentId);

    ResponseEntity<List<CommentDto>> getRecipeComments(Long id);
}
