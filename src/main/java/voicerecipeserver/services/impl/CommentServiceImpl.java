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
import voicerecipeserver.security.service.impl.AuthServiceCommon;
import voicerecipeserver.services.CommentService;
import voicerecipeserver.utils.FindUtils;

import java.util.List;

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
        User user = FindUtils.findUser(userRepository, commentDto.getUserUid());
        Recipe recipe = FindUtils.findRecipe(recipeRepository, commentDto.getRecipeId());
        comment.setUser(user);
        comment.setRecipe(recipe);

        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(new IdDto().id(savedComment.getId()));
    }

    @Override
    public ResponseEntity<IdDto> updateComment(CommentDto commentDto) throws NotFoundException {
        Comment comment = FindUtils.findComment(commentRepository, commentDto.getId());
        if (AuthServiceCommon.checkAuthorities(comment.getUser().getUid())) {
            comment.setContent(commentDto.getContent());
        }
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(new IdDto().id(savedComment.getId()));
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long commentId) throws NotFoundException {
        Comment comment = FindUtils.findComment(commentRepository, commentId);
        if (AuthServiceCommon.checkAuthorities(comment.getUser().getUid())) {
            commentRepository.deleteById(commentId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CommentDto>> getRecipeComments(Long id) {
        List<Comment> comments = commentRepository.getCommentsByRecipeId(id);
        List<CommentDto> dtos = comments.stream().map((comment) -> mapper.map(comment, CommentDto.class)).toList();
        return ResponseEntity.ok(dtos);
    }
}
