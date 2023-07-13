package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.CommentApi;
import voicerecipeserver.model.dto.CommentDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.CommentService;

import java.util.List;

@CrossOrigin(maxAge = 1440)
@RestController
public class CommentApiController implements CommentApi {
    private final CommentService commentService;

    @Autowired
    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public ResponseEntity<IdDto> commentPost(CommentDto commentDto) throws NotFoundException {
        System.out.println("COMMENT DTO:");
        System.out.println(commentDto);
        return commentService.postComment(commentDto);
    }

    @Override
    public ResponseEntity<IdDto> commentUpdate(CommentDto commentDto) throws NotFoundException {
        return commentService.updateComment(commentDto);
    }

    @Override
    public ResponseEntity<Void> commentDelete(Long id) {
        return commentService.deleteComment(id);
    }

    @Override
    public ResponseEntity<List<CommentDto>> getRecipeComments(Long id) {
        return commentService.getRecipeComments(id);
    }
}
