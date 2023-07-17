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
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.security.service.impl.AuthServiceImpl;
import voicerecipeserver.services.CommentService;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final ModelMapper mapper;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AuthServiceImpl authentication;

    @Autowired
    public CommentServiceImpl(ModelMapper mapper, RecipeRepository recipeRepository, UserRepository userRepository,
                              CommentRepository commentRepository, AuthServiceImpl authentication) {
        this.mapper = mapper;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.authentication = authentication;
    }

    Comment findComment(Long commentId) throws NotFoundException {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти комментарий с id: " + commentId);
        }
        return commentOptional.get();
    }

    User findUser(String userUid) throws NotFoundException {
        Optional<User> userOptional = userRepository.findByUid(userUid);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти пользователя с uid: " + userUid);
        }
        return userOptional.get();
    }

    private Recipe findRecipe(Long id) throws NotFoundException {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти рецепт с id: " + id);
        }
        return recipeOptional.get();
    }

    @Override
    public ResponseEntity<IdDto> postComment(CommentDto commentDto) throws NotFoundException {
        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setId(null);
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
        if (authentication != null && checkAuthorities(commentDto.getId())) {
            comment.setContent(commentDto.getContent());
        }
        Comment savedComment = commentRepository.save(comment);
        return new ResponseEntity<>(new IdDto().id(savedComment.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long commentId) throws NotFoundException {
        if (authentication != null && checkAuthorities(commentId)) {
            commentRepository.deleteById(commentId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CommentDto>> getRecipeComments(Long id) {
        List<Comment> comments = commentRepository.getCommentsByRecipeId(id);
        List<CommentDto> dtos = comments.stream().map((comment) -> mapper.map(comment, CommentDto.class)).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    private boolean checkAuthorities(Long markId) throws NotFoundException {
        JwtAuthentication principal = authentication.getAuthInfo();
        Comment comment = findComment(markId);
        User user = comment.getUser();
        return principal.getAuthorities().contains(Role1.ADMIN) || principal.getLogin().equals(user.getUid());
    }
}
