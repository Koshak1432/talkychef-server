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
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.MarkDto;
import talkychefserver.model.entities.Mark;
import talkychefserver.model.entities.MarkKey;
import talkychefserver.model.entities.Recipe;
import talkychefserver.model.entities.User;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;
import talkychefserver.respositories.MarkRepository;
import talkychefserver.respositories.RecipeRepository;
import talkychefserver.respositories.UserRepository;
import talkychefserver.security.service.impl.AuthServiceCommon;
import talkychefserver.services.interfaces.MarkService;
import talkychefserver.utils.FindUtils;

import java.util.Optional;

@Slf4j
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

    private void setRecipeToMark(Mark mark, Long recipeId) {
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        mark.getId().setRecipeId(recipe.getId());
        mark.setRecipe(recipe);
    }

    private void setAuthorToMark(Mark mark, String uid) {
        User author = FindUtils.findUserByUid(userRepository, uid);
        mark.getId().setUserId(author.getId());
        mark.setUser(author);
    }

    @Override
    public ResponseEntity<Float> getAvgMark(Long recipeId) {
        log.info("Processing get avg mark of recipe [{}] request", recipeId);
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        Float res = 0f;
        if (recipe.getAvgMark() != null) {
            res = recipe.getAvgMark().getAvgMark();
        }
        log.info("Recipe [{}] avg mark: {}", recipeId, res);
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<MarkDto> getRecipeMark(String userUid, Long recipeId) {
        log.info("Processing get recipe [{}] mark by user [{}] request", recipeId, userUid);
        User user = FindUtils.findUserByUid(userRepository, userUid);
        Optional<Mark> mark = markRepository.findById(new MarkKey(user.getId(), recipeId));
        if (mark.isEmpty()) {
            log.error("Couldn't find mark by user [{}] for recipe [{}]", userUid, recipeId);
            throw new NotFoundException("Couldn't find mark by " + userUid + " for recipe with id: " + recipeId);
        }
        log.info("Recipe mark by user [{}] for recipe [{}]: {}", userUid, recipeId, mark.get().getMark());
        return ResponseEntity.ok(mapper.map(mark, MarkDto.class));
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> addRecipeMark(MarkDto markDto) {
        log.info("Processing add recipe mark request");
        if (!AuthServiceCommon.checkAuthorities(markDto.getUserUid())) {
            log.error("Has no rights to add recipe mark");
            throw new AuthException("Has no rights");
        }
        // todo change if
        Mark mark = mapper.map(markDto, Mark.class);
        setRecipeToMark(mark, markDto.getRecipeId());
        setAuthorToMark(mark, markDto.getUserUid());
        if (!markIsPresent(mark)) {
            markRepository.save(mark);
        } else {
            log.error("The mark by user [{}] for recipe [{}] already exists", markDto.getUserUid(),
                     markDto.getRecipeId());
            throw new BadRequestException("The mark already exist");
        }
        return ResponseEntity.ok(new IdDto().id(mark.getId().getRecipeId()));
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> updateRecipeMark(MarkDto markDto) {
        log.info("Processing update recipe mark request");
        if (!AuthServiceCommon.checkAuthorities(markDto.getUserUid())) {
            log.error("User has no rights to update recipe mark");
            throw new AuthException("No rights");
        }
        // todo change if
        Mark newMark = mapper.map(markDto, Mark.class);
        setRecipeToMark(newMark, markDto.getRecipeId());
        setAuthorToMark(newMark, markDto.getUserUid());
        if (markIsPresent(newMark)) {
            markRepository.save(newMark);
        } else {
            log.error("Couldn't find mark");
            throw new NotFoundException("Couldn't find the mark");
        }
        log.info("Update recipe [{}] mark by user [{}]. Mark: {}", markDto.getRecipeId(), markDto.getUserUid(),
                 markDto.getMark());
        return ResponseEntity.ok(new IdDto().id(newMark.getId().getRecipeId()));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteRecipeMark(String userUid, Long recipeId) {
        log.info("Processing delete recipe [{}] mark by user [{}]", recipeId, userUid);
        if (!AuthServiceCommon.checkAuthorities(userUid)) {
            log.error("User [{}] has no rights to delete recipe [{}] mark", userUid, recipeId);
            throw new AuthException("No rights");
        }
        User user = FindUtils.findUserByUid(userRepository, userUid);
        markRepository.deleteById(new MarkKey(user.getId(), recipeId));
        log.info("Deleted recipe [{}] mark by user [{}]", recipeId, userUid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean markIsPresent(Mark mark) {
        Optional<Mark> markOptional = markRepository.findById(mark.getId());
        return markOptional.isPresent();
    }
}

