package talkychefserver.services;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.CommentDto;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.exceptions.NotFoundException;

import java.util.List;

public interface CommentService {
    ResponseEntity<IdDto> postComment(CommentDto commentDto) throws NotFoundException;

    ResponseEntity<IdDto> updateComment(CommentDto commentDto) throws NotFoundException;

    ResponseEntity<Void> deleteComment(Long commentId) throws NotFoundException;

    ResponseEntity<List<CommentDto>> getRecipeComments(Long id);
}
