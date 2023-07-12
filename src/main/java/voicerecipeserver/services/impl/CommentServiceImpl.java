package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.CommentDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.entities.Comment;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.CommentRepository;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.services.CommentService;

import java.util.Date;

@Service
public class CommentServiceImpl implements CommentService {
    private final ModelMapper mapper;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(ModelMapper mapper, RecipeRepository recipeRepository, UserRepository userRepository,
                              CommentRepository commentRepository) {
        this.mapper = mapper;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ResponseEntity<IdDto> postComment(CommentDto commentDto) throws NotFoundException {
        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setId(null);
        comment.setDate(new Date());
        User user = findUser(commentDto.getUserUid());
        Recipe recipe = findRecipe(commentDto.getRecipeId());
        comment.setUser(user);
        comment.setRecipe(recipe);

        Comment savedComment = commentRepository.save(comment);
        return new ResponseEntity<>(new IdDto().id(savedComment.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> updateComment(CommentDto commentDto) throws NotFoundException {
        Comment comment = findComment(commentDto.getId());
        comment.setContent(commentDto.getContent());
        Comment savedComment = commentRepository.save(comment);
        return new ResponseEntity<>(new IdDto().id(savedComment.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
