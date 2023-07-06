package voicerecipeserver.services.impl;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.CommentDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.*;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.*;
import voicerecipeserver.services.RecipeService;

import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final MeasureUnitRepository measureUnitRepository;
    private final IngredientsDistributionRepository ingredientsDistributionRepository;
    private final StepRepository stepRepository;
    private final MediaRepository mediaRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private final ModelMapper mapper;

    // TODO не ну это колбасу точно убрать надо, мб по-другому внедрить
    @Autowired
    public RecipeServiceImpl(StepRepository stepRepository, RecipeRepository recipeRepository,
                             IngredientRepository ingredientRepository, MeasureUnitRepository measureUnitRepository,
                             IngredientsDistributionRepository ingredientsDistributionRepository,
                             MediaRepository mediaRepository, CommentRepository commentRepository, ModelMapper mapper) {
        this.stepRepository = stepRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.measureUnitRepository = measureUnitRepository;
        this.ingredientsDistributionRepository = ingredientsDistributionRepository;
        this.mediaRepository = mediaRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;

        this.mapper.typeMap(Recipe.class, RecipeDto.class).addMappings(
                m -> m.map(src -> src.getAuthor().getUid(), RecipeDto::setAuthorId));
    }

    @Autowired
    private void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<RecipeDto> getRecipeById(Long id) throws NotFoundException {
        Recipe recipe = findRecipe(id);
        RecipeDto recipeDto = mapper.map(recipe, RecipeDto.class);
        return new ResponseEntity<>(recipeDto, HttpStatus.OK);
    }

    private Recipe findRecipe(Long id) throws NotFoundException {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти рецепт с id: " + id);
        }
        return recipeOptional.get();
    }

    @Override
    public ResponseEntity<IdDto> addRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException {
        Recipe recipe = mapper.map(recipeDto, Recipe.class);

        setAuthorTo(recipe);
        recipe.setId(null);

        // через маппер можно сделать путем добавления конвертера. Только вот код
        // там будет хуже, его будет сильно больше, а производительность вряд ли вырастет
        for (Step step : recipe.getSteps()) {
            step.setRecipe(recipe);
        }

        setDistribution(recipe);
        recipeRepository.save(recipe);
        return new ResponseEntity<>(new IdDto().id(recipe.getId()), HttpStatus.OK);
    }

    private void setAuthorTo(Recipe recipe) throws NotFoundException {
        Optional<User> author = userRepository.findByUid(recipe.getAuthor().getUid());

        if (author.isEmpty()) {
            throw new NotFoundException("Не удалось найти автора с uid: " + recipe.getAuthor().getUid());
        } else {
            recipe.setAuthor(author.get());
        }
    }

    private void setDistribution(Recipe recipe) throws BadRequestException {
        HashSet<String> ingredientsInRecipe = new HashSet<>();
        for (IngredientsDistribution ingredientsDistribution : recipe.getIngredientsDistributions()) {
            ingredientsDistribution.setRecipe(recipe);

            Ingredient receivedIngredient = ingredientsDistribution.getIngredient();
            String ingredientName = receivedIngredient.getName();

            if (ingredientsInRecipe.contains(ingredientName)) {
                throw new BadRequestException("Ингредиент встречается дважды: " + ingredientName);
            }
            ingredientsInRecipe.add(receivedIngredient.getName());


            Optional<Ingredient> ingredientFromRepo = ingredientRepository.findByName(ingredientName);
            if (ingredientFromRepo.isEmpty()) {
                ingredientsDistribution.setIngredient(ingredientRepository.save(receivedIngredient));
            } else {
                ingredientsDistribution.setIngredient(ingredientFromRepo.get());
                ingredientsDistribution.setId(
                        new IngredientsDistributionKey(recipe.getId(), ingredientFromRepo.get().getId()));
            }
            //TODO в идеале бы настроить для recipe save так, чтобы он сохранял measureUnit, ingredient, если их нет
            // в БД. Хочется убрать лишний find.

            setMeasureUnit(ingredientsDistribution);
        }
    }

    private void setMeasureUnit(IngredientsDistribution ingredientsDistribution) {
        Optional<MeasureUnit> measureUnitOptional = measureUnitRepository.findByName(
                ingredientsDistribution.getUnit().getName());
        if (measureUnitOptional.isEmpty()) {
            ingredientsDistribution.setUnit(measureUnitRepository.save(ingredientsDistribution.getUnit()));
        } else {
            ingredientsDistribution.setUnit(measureUnitOptional.get());
        }
    }

    @Override
    public ResponseEntity<IdDto> updateRecipe(RecipeDto recipeDto, Long id) throws NotFoundException,
            BadRequestException {
        Recipe oldRecipe = findRecipe(id);
        Recipe newRecipe = mapper.map(recipeDto, Recipe.class);
        newRecipe.setId(id);
        setAuthorTo(newRecipe);
        setSteps(oldRecipe, newRecipe);
        setDistribution(newRecipe);

        Set<Long> unusedMediaIds = getUnusedMediaIds(oldRecipe, newRecipe);

        recipeRepository.save(newRecipe);
        mediaRepository.deleteAllById(unusedMediaIds);
        return new ResponseEntity<>(new IdDto().id(newRecipe.getId()), HttpStatus.OK);
    }

    private static Set<Long> getUnusedMediaIds(Recipe oldRecipe, Recipe newRecipe) {
        int defaultMediaId = 172;
        Set<Long> oldRecipeMedia = getRecipeMedia(oldRecipe);
        Set<Long> newRecipeMedia = getRecipeMedia(newRecipe);
        Set<Long> unusedMedia = new HashSet<>();
        for (Long mediaId : oldRecipeMedia) {
            if (! newRecipeMedia.contains(mediaId) && mediaId != defaultMediaId) {
                unusedMedia.add(mediaId);
            }
        }
        return unusedMedia;
    }

    private static void setSteps(Recipe oldRecipe, Recipe newRecipe) {
        List<Step> oldSteps = oldRecipe.getSteps();
        List<Step> newSteps = newRecipe.getSteps();
        oldSteps.sort(Comparator.comparingInt(Step::getStepNum));
        newSteps.sort(Comparator.comparingInt(Step::getStepNum));

        // rest of the oldSteps will be deleted automatically because of orphanRemoval = true
        for (int i = 0; i < newSteps.size(); ++ i) {
            Step newStep = newSteps.get(i);
            if (i < oldSteps.size()) {
                newStep.setId(oldSteps.get(i).getId());
            }
            newStep.setRecipe(newRecipe);
        }
    }

    private static Set<Long> getRecipeMedia(Recipe recipe) {
        Set<Long> recipeMedia = new HashSet<>();
        recipeMedia.add(recipe.getMedia().getId());
        for (Step step : recipe.getSteps()) {
            Media media = step.getMedia();
            if (media != null) {
                recipeMedia.add(media.getId());
            }
        }
        return recipeMedia;
    }

    User findUser(String userUid) throws NotFoundException {
        Optional<User> userOptional = userRepository.findByUid(userUid);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти пользователя с UID: " + userUid);
        }
        return userOptional.get();
    }

    @Override
    public ResponseEntity<List<RecipeDto>> searchRecipesByName(String name, Integer limit) throws NotFoundException {
        if (limit == null) {
            limit = 0;
        }
        List<Recipe> recipes = recipeRepository.findByNameContaining(name, limit);

        if (recipes.isEmpty()) {
            throw new NotFoundException("Не удалось найти рецепты с подстрокой: " + name);
        }
        List<RecipeDto> recipeDtos = mapper.map(recipes, new TypeToken<List<RecipeDto>>() {}.getType());

        return new ResponseEntity<>(recipeDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> postComment(CommentDto commentDto) throws NotFoundException, BadRequestException {
        Comment comment = mapper.map(commentDto, Comment.class);
        // todo найти юзера и рецепт, добавить к ним этот комментарий, потому что сам он не добавится
        User user = findUser(commentDto.getUserUid());
        Recipe recipe = findRecipe(commentDto.getRecipeId());
        user.getComments().add(comment);
        recipe.getComments().add(comment);
        Comment savedComment = commentRepository.save(comment);
        return new ResponseEntity<>(new IdDto().id(savedComment.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> updateComment(CommentDto commentDto) throws NotFoundException, BadRequestException {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long recipeId, Long commentId) throws NotFoundException,
            BadRequestException {
        return null;
    }
}
