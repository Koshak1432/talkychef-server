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
import voicerecipeserver.model.entities.MarkKey;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
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
    public MarkServiceImpl(ModelMapper mapper, RecipeRepository recipeRepository, UserRepository userRepository,
                           MarkRepository markRepository, AuthServiceImpl authentication) {
        this.mapper = mapper;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.markRepository = markRepository;
        this.authentication = authentication;
    }

    private void setRecipeToMark(Mark mark, Long recipeId) throws NotFoundException {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            throw new NotFoundException("Не удалось найти рецепт с id: " + mark.getId().getRecipeId());
        } else {
            mark.getId().setRecipeId(recipe.get().getId());
            mark.setRecipe(recipe.get());
        }
    }

    private void setAuthorToMark(Mark mark, String uid) throws NotFoundException {
        Optional<User> author = userRepository.findByUid(uid);
        if (author.isEmpty()) {
            throw new NotFoundException("Не удалось найти пользователя с uid: " + mark.getId().getUserId());
        } else {
            mark.getId().setUserId(author.get().getId());
            mark.setUser(author.get());
        }
    }

    @Override
    public ResponseEntity<IdDto> addRecipeMark(MarkDto markDto) throws NotFoundException, AuthException,
            BadRequestException {
        Mark mark = mapper.map(markDto, Mark.class);
        if (!isSamePerson(markDto.getUserUid())) {
            throw new BadRequestException("Нельзя добавлять комментарии от чужого имени");
        }
        setRecipeToMark(mark, markDto.getRecipeId());
        setAuthorToMark(mark, markDto.getUserUid());
        if (!markIsPresent(mark)) {
            markRepository.save(mark);
        }
        return ResponseEntity.ok(new IdDto().id(mark.getId().getRecipeId()));
    }

    @Override
    public ResponseEntity<IdDto> updateRecipeMark(MarkDto markDto) throws NotFoundException, BadRequestException,
            AuthException {
        Mark newMark = mapper.map(markDto, Mark.class);
        if (!checkAuthorities(markDto.getUserUid())) {
            throw new BadRequestException("Нет прав на изменение оценки");
        }
        setRecipeToMark(newMark, markDto.getRecipeId());
        setAuthorToMark(newMark, markDto.getUserUid());
        if (markIsPresent(newMark)) {
            markRepository.save(newMark);
        } else {
            throw new NotFoundException("Предыдущая оценка не найдена");
        }
        return ResponseEntity.ok(new IdDto().id(newMark.getId().getRecipeId()));
    }

    @Override
    public ResponseEntity<Void> deleteRecipeMark(String userUid, Long recipeId) throws BadRequestException {
        if (!checkAuthorities(userUid)) {
            throw new BadRequestException("Нет прав на удаление оценки");
        }
        Optional<User> user = userRepository.findByUid(userUid);
        if (user.isPresent()) {
            Long userId = user.get().getId();
            markRepository.deleteByIdUserIdAndIdRecipeId(userId, recipeId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isContainsRole(Collection<? extends GrantedAuthority> authorities, String name) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority() != null && authority.getAuthority().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSamePerson(String userUid) {
        if (authentication == null) {
            return false;
        }
        JwtAuthentication principal = authentication.getAuthInfo();
        return principal.getLogin().equals(userUid);
    }

    private boolean checkAuthorities(String userUid) {
        if (authentication == null) {
            return false;
        }
        JwtAuthentication principal = authentication.getAuthInfo();
        return isContainsRole(principal.getAuthorities(), "ADMIN") || principal.getLogin().equals(userUid);
    }

    private boolean markIsPresent(Mark mark) {
        Optional<Mark> markOptional = markRepository.findByUserIdAndRecipeId(mark.getId().getUserId(),
                                                                             mark.getId().getRecipeId());
        if (markOptional.isPresent()) {
            System.out.println("++++++++++++++++++++++++++++");
            System.out.println("MARK OPTIONAL: " + markOptional.get());
            System.out.println("++++++++++++++++++++++++++++");
        }
        return markOptional.isPresent();
    }
}

