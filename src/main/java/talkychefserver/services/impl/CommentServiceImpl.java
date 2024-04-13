package talkychefserver.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkychefserver.model.dto.CommentDto;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.entities.Comment;
import talkychefserver.model.entities.Recipe;
import talkychefserver.model.entities.User;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.respositories.CommentRepository;
import talkychefserver.respositories.RecipeRepository;
import talkychefserver.respositories.UserRepository;
import talkychefserver.security.service.impl.AuthServiceCommon;
import talkychefserver.services.interfaces.CommentService;
import talkychefserver.utils.FindUtils;

import java.util.List;

@Slf4j
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
    @Transactional
    public ResponseEntity<IdDto> postComment(CommentDto commentDto) {
        log.info("Processing add comment request");
        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setId(null);
        User user = FindUtils.findUserByUid(userRepository, commentDto.getUserUid());
        Recipe recipe = FindUtils.findRecipe(recipeRepository, commentDto.getRecipeId());
        comment.setUser(user);
        comment.setRecipe(recipe);
        Comment savedComment = commentRepository.save(comment);
        log.info("Added comment from user [{}] to recipe [{}]", user.getUid(), recipe.getId());
        return ResponseEntity.ok(new IdDto().id(savedComment.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> updateComment(CommentDto commentDto) {
        log.info("Processing update comment request");
        Comment comment = FindUtils.findComment(commentRepository, commentDto.getId());
        if (!AuthServiceCommon.checkAuthorities(comment.getUser().getUid())) {
            log.error("User has no rights to update comment [{}]", comment.getId());
            throw new AuthException("Has no rights");
        }
        comment.setContent(commentDto.getContent());
        Comment savedComment = commentRepository.save(comment);
        log.info("Updated comment [{}]. It's content: [{}]", savedComment.getId(), savedComment.getContent());
        return ResponseEntity.ok(new IdDto().id(savedComment.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteComment(Long commentId) {
        log.info("Processing delete comment [{}] request", commentId);
        Comment comment = FindUtils.findComment(commentRepository, commentId);
        if (!AuthServiceCommon.checkAuthorities(comment.getUser().getUid())) {
            log.error("User has no rights to delete comment [{}]", commentId);
            throw new AuthException("Has no rights");
        }
        commentRepository.deleteById(commentId);
        log.info("Deleted comment [{}]", commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CommentDto>> getRecipeComments(Long id) {
        log.info("Processing get recipe [{}] comments request", id);
        List<Comment> comments = commentRepository.getCommentsByRecipeId(id);
        List<CommentDto> dtos = comments.stream().map((comment) -> mapper.map(comment, CommentDto.class)).toList();
        log.info("Response comment list size: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }
}
