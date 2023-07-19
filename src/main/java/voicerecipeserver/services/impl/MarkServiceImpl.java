package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarkDto;
import voicerecipeserver.model.entities.Mark;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.MarkRepository;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.security.service.impl.AuthServiceImpl;
import voicerecipeserver.services.MarkService;

import java.util.Collection;
import java.util.Optional;

@Service

public class MarkServiceImpl implements MarkService {
    private final ModelMapper mapper;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    private final MarkRepository markRepository;
    private final AuthServiceImpl authentication;

    @Autowired
    public MarkServiceImpl(ModelMapper mapper, RecipeRepository recipeRepository, UserRepository userRepository, MarkRepository markRepository, AuthServiceImpl authentication) {
        this.mapper = mapper;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.markRepository = markRepository;
        this.authentication = authentication;
    }

    private void setRecipeToMark(Mark mark) throws NotFoundException {
        Optional<Recipe> recipe = recipeRepository.findById(mark.getId().getRecipeId());
        if (recipe.isEmpty()) {
            throw new NotFoundException("Не удалось найти рецепт с id: " + mark.getId().getRecipeId());
        } else {
            mark.getId().setRecipeId(recipe.get().getId());
            mark.setRecipe(recipe.get());
        }
    }

    private void setAuthorToMark(Mark mark) throws NotFoundException {
        Optional<User> author = userRepository.findById(mark.getId().getUserId());
        if (author.isEmpty()) {
            throw new NotFoundException("Не удалось найти автора с uid: " + mark.getId().getUserId());
        } else {
            mark.getId().setUserId(author.get().getId());
            mark.setUser(author.get());
        }
    }

    @Override
    public ResponseEntity<IdDto> addRecipeMark(MarkDto markDto) throws NotFoundException, AuthException {
        Mark mark = mapper.map(markDto, Mark.class);
        Optional<User> user = userRepository.findByUid(markDto.getUserUid());
        if (user.isPresent()) {
            mark.getId().setUserId(user.get().getId());
        } else {
            throw new AuthException("Такого пользователя не существует");
        }
        Optional<Mark> markOptional = markRepository.findByUserIdAndRecipeId(mark.getId().getUserId(), mark.getId().getRecipeId());
        if (markOptional.isEmpty()) {
            setRecipeToMark(mark);
            setAuthorToMark(mark);
            markRepository.save(mark);
        }
        return new ResponseEntity<>(new IdDto().id(mark.getId().getRecipeId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> updateRecipeMark(MarkDto markDto) throws NotFoundException {
        Mark newMark = mapper.map(markDto, Mark.class);
        if (authentication != null && checkAuthorities(markDto.getUserUid())) {
            if (markIsPresent(newMark)) {
                markRepository.save(newMark);
            }
        }
        return new ResponseEntity<>(new IdDto().id(newMark.getId().getRecipeId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteRecipeMark(String userUid, Long recipeId) throws NotFoundException {
        if (authentication != null && checkAuthorities(userUid)) {
            Optional<User> user = userRepository.findByUid(userUid);
            if (user.isPresent()) {
                Long userId = user.get().getId();
                markRepository.deleteByIdUserIdAndIdRecipeId(userId, recipeId);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isContainsRoleName(Collection<? extends GrantedAuthority> authorities, String name) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority() != null && authority.getAuthority().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAuthorities(String userUid) {
        JwtAuthentication principal = authentication.getAuthInfo();
        return isContainsRoleName(principal.getAuthorities(), "ADMIN") || principal.getLogin().equals(userUid);
    }


    private boolean markIsPresent(Mark mark) throws NotFoundException {
        Optional<Mark> markOptional = markRepository.findByUserIdAndRecipeId(mark.getId().getUserId(), mark.getId().getRecipeId());
        return markOptional.isPresent();
    }
}

