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
        Optional<Recipe> recipe = recipeRepository.findById(mark.getRecipe().getId());
        if (recipe.isEmpty()) {
            throw new NotFoundException("Не удалось найти рецепт с id: " + mark.getRecipe().getId());
        } else {
            mark.setRecipe(recipe.get());
        }
    }

    private void setAuthorToMark(Mark mark) throws NotFoundException {
        Optional<User> author = userRepository.findByUid(mark.getUser().getUid());
        if (author.isEmpty()) {
            throw new NotFoundException("Не удалось найти автора с uid: " + mark.getUser().getUid());
        } else {
            mark.setUser(author.get());
        }
    }

    @Override
    public ResponseEntity<IdDto> addRecipeMark(MarkDto markDto) throws NotFoundException {
        Mark mark = mapper.map(markDto, Mark.class);
        setRecipeToMark(mark);
        setAuthorToMark(mark);
        mark.setId(null);
        markRepository.save(mark);
        return new ResponseEntity<>(new IdDto().id(mark.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> updateRecipeMark(MarkDto markDto) throws NotFoundException {
        Mark newMark = mapper.map(markDto, Mark.class);
        if (authentication != null && checkAuthorities(markDto.getId())) {
            newMark.setId(markDto.getId());
            setAuthorToMark(newMark);
            markRepository.save(newMark);
        }
        return new ResponseEntity<>(new IdDto().id(newMark.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteRecipeMark(Long markId) throws NotFoundException {
        if (authentication != null && checkAuthorities(markId)) {
            markRepository.deleteById(markId);
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

    private boolean checkAuthorities(Long markId) throws NotFoundException {
        JwtAuthentication principal = authentication.getAuthInfo();
        Mark mark = findMark(markId);
        User user = mark.getUser();
        return isContainsRoleName(principal.getAuthorities(), "ADMIN") || principal.getLogin().equals(user.getUid());
    }


    private Mark findMark(Long id) throws NotFoundException {
        Optional<Mark> markOptional = markRepository.findById(id);
        if (markOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти оценку с id: " + id);
        }
        return markOptional.get();
    }
}

