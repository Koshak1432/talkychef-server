package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import voicerecipeserver.security.service.impl.AuthServiceCommon;
import voicerecipeserver.services.MarkService;
import voicerecipeserver.utils.FindUtils;

import java.util.Optional;

import static voicerecipeserver.security.service.impl.AuthServiceCommon.checkAuthorities;

@Service

public class MarkServiceImpl implements MarkService {
    private final ModelMapper mapper;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final MarkRepository markRepository;

    @Autowired
    public MarkServiceImpl(ModelMapper mapper, RecipeRepository recipeRepository, UserRepository userRepository,
                           MarkRepository markRepository) {
        this.mapper = mapper;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.markRepository = markRepository;
    }

    private void setRecipeToMark(Mark mark, Long recipeId) throws NotFoundException {
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        mark.getId().setRecipeId(recipe.getId());
        mark.setRecipe(recipe);
    }

    private void setAuthorToMark(Mark mark, String uid) throws NotFoundException {
        User author = FindUtils.findUserByUid(userRepository, uid);
        mark.getId().setUserId(author.getId());
        mark.setUser(author);
    }

    @Override
    public ResponseEntity<Float> getAvgMark(Long recipeId) throws NotFoundException {
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        Float res = 0f;
        if (recipe.getAvgMark() != null) {
            res = recipe.getAvgMark().getAvgMark();
        }
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<MarkDto> getRecipeMark(String userUid, Long recipeId) throws NotFoundException {
        User user = FindUtils.findUserByUid(userRepository, userUid);
        Optional<Mark> mark = markRepository.findById(new MarkKey(user.getId(), recipeId));
        if (mark.isEmpty()) {
            throw new NotFoundException("Couldn't find mark by " + userUid + " for recipe with id: " + recipeId);
        }
        return ResponseEntity.ok(mapper.map(mark, MarkDto.class));
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> addRecipeMark(MarkDto markDto) throws NotFoundException, AuthException,
            BadRequestException {
        Mark mark = mapper.map(markDto, Mark.class);
        if (!AuthServiceCommon.isSamePerson(markDto.getUserUid())) {
            throw new AuthException("You cannot add mark from another user");
        }
        setRecipeToMark(mark, markDto.getRecipeId());
        setAuthorToMark(mark, markDto.getUserUid());
        if (!markIsPresent(mark)) {
            markRepository.save(mark);
        } else {
            throw new BadRequestException("The mark already exist");
        }
        return ResponseEntity.ok(new IdDto().id(mark.getId().getRecipeId()));
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> updateRecipeMark(MarkDto markDto) throws NotFoundException, AuthException {
        Mark newMark = mapper.map(markDto, Mark.class);
        if (!AuthServiceCommon.checkAuthorities(markDto.getUserUid())) {
            throw new AuthException("No rights");
        }
        setRecipeToMark(newMark, markDto.getRecipeId());
        setAuthorToMark(newMark, markDto.getUserUid());
        if (markIsPresent(newMark)) {
            markRepository.save(newMark);
        } else {
            throw new NotFoundException("Couldn't find previous mark");
        }
        return ResponseEntity.ok(new IdDto().id(newMark.getId().getRecipeId()));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteRecipeMark(String userUid, Long recipeId) throws AuthException,
            NotFoundException {
        if (!AuthServiceCommon.checkAuthorities(userUid)) {
            throw new AuthException("No rights");
        }
        User user = FindUtils.findUserByUid(userRepository, userUid);
        markRepository.deleteById(new MarkKey(user.getId(), recipeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean markIsPresent(Mark mark) {
        Optional<Mark> markOptional = markRepository.findById(mark.getId());
        return markOptional.isPresent();
    }
}

