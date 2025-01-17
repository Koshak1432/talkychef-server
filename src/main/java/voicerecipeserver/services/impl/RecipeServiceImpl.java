package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.Collection;
import voicerecipeserver.model.entities.*;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.recommend.SlopeOne;
import voicerecipeserver.respository.*;
import voicerecipeserver.security.service.impl.AuthServiceCommon;
import voicerecipeserver.services.RecipeService;
import voicerecipeserver.utils.FindUtils;
import voicerecipeserver.utils.GetUtil;

import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final ModelMapper mapper;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final MeasureUnitRepository measureUnitRepository;
    private final UserRepository userRepository;
    private final StepRepository stepRepository;
    private final MarkRepository markRepository;
    private final MediaRepository mediaRepository;
    private final CollectionRepository collectionRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository,
                             MeasureUnitRepository measureUnitRepository, ModelMapper mapper,
                             AvgMarkRepository avgMarkRepository, StepRepository stepRepository,
                             MarkRepository markRepository, UserRepository userRepository,
                             MediaRepository mediaRepository, CollectionRepository collectionRepository,
                             CategoryRepository categoryRepository) {

        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.measureUnitRepository = measureUnitRepository;
        this.stepRepository = stepRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.markRepository = markRepository;
        this.mediaRepository = mediaRepository;
        this.collectionRepository = collectionRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public ResponseEntity<RecipeDto> getRecipeById(Long id) throws NotFoundException {
        Recipe recipe = FindUtils.findRecipe(recipeRepository, id);
        RecipeDto recipeDto = mapper.map(recipe, RecipeDto.class);
        return ResponseEntity.ok(recipeDto);
    }

    private void checkRecipeMediaUniqByStep(Long stepMediaId) throws BadRequestException {
        if (recipeRepository.findRecipeByMediaId(stepMediaId).isPresent()) {
            throw new BadRequestException("Media id must be unique");
        }
    }

    private void checkStepMediaUniqByRecipe(Long recipeMediaId) throws BadRequestException {
        if (stepRepository.findStepByMediaId(recipeMediaId).isPresent()) {
            throw new BadRequestException("Media id must be unique");
        }
    }

    private void checkRecipeMediaUniq(Long recipeMediaId) throws BadRequestException {
        if (recipeRepository.findRecipeByMediaId(recipeMediaId).isPresent()) {
            throw new BadRequestException("Media id must be unique");
        }
    }

    private void checkMediaUniqueness(Recipe recipe) throws BadRequestException {
        checkStepMediaUniqByRecipe(recipe.getMedia().getId());
        checkRecipeMediaUniq(recipe.getMedia().getId());
        Set<Long> mediaIds = new HashSet<>();
        mediaIds.add(recipe.getMedia().getId());

        for (Step step : recipe.getSteps()) {
            if (step.getMedia() == null) {
                continue;
            }
            checkRecipeMediaUniqByStep(step.getMedia().getId());
            Long mediaId = step.getMedia().getId();
            if (mediaIds.contains(mediaId)) {
                throw new BadRequestException("Media id must be unique");
            } else {
                mediaIds.add(mediaId);
            }
        }
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> addRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException,
            AuthException {
        if (!AuthServiceCommon.checkAuthorities(recipeDto.getAuthorUid())) {
            throw new AuthException("No rights");
        }
        if (recipeDto.getMediaId() == null) {
            throw new BadRequestException("Media id must be present");
        }
        Recipe recipe = mapper.map(recipeDto, Recipe.class);
        User author = FindUtils.findUserByUid(userRepository, recipe.getAuthor().getUid());
        recipe.setAuthor(author);
        recipe.setId(null);
        if (mediaRepository.findById(recipe.getMedia().getId()).isEmpty()) {
            throw new NotFoundException("Couldn't find media with id: " + recipe.getMedia().getId());
        }
        checkMediaUniqueness(recipe);
        // через маппер можно сделать путем добавления конвертера. Только вот код
        // там будет хуже, его будет сильно больше, а производительность вряд ли вырастет
        for (Step step : recipe.getSteps()) {
            step.setRecipe(recipe);
        }

        setDistribution(recipe);
        Recipe savedRecipe = recipeRepository.save(recipe);
        String savedName = author.getUid() + "_saved";
        Collection saveCollection = collectionRepository.findByAuthorIdUserRecipeCollection(author.getId(),
                                                                                                savedName).orElse(null);
        if (saveCollection == null) {
            saveCollection = collectionRepository.save(new Collection(savedName, 0, author));
        }
        collectionRepository.addRecipeToCollection(savedRecipe.getId(), saveCollection.getId());
        return ResponseEntity.ok(new IdDto().id(savedRecipe.getId()));
    }

    private void setAuthorToRecipe(Recipe recipe) throws NotFoundException {
        User author = FindUtils.findUserByUid(userRepository, recipe.getAuthor().getUid());
        recipe.setAuthor(author);
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> updateRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException,
            AuthException {

        if (!AuthServiceCommon.checkAuthorities(recipeDto.getAuthorUid())) {
            throw new AuthException("No rights");
        }
        Recipe oldRecipe = FindUtils.findRecipe(recipeRepository, recipeDto.getId());
        Recipe newRecipe = mapper.map(recipeDto, Recipe.class);

        newRecipe.setId(recipeDto.getId());
        setAuthorToRecipe(newRecipe);
        setSteps(oldRecipe, newRecipe);
        checkMediaUniqueness(newRecipe);
        setDistribution(newRecipe);
        recipeRepository.save(newRecipe);
        return ResponseEntity.ok(new IdDto().id(newRecipe.getId()));
    }

    private void setDistribution(Recipe recipe) throws BadRequestException {
        HashSet<String> ingredientsInRecipe = new HashSet<>();
        for (IngredientsDistribution ingredientsDistribution : recipe.getIngredientsDistributions()) {
            ingredientsDistribution.setRecipe(recipe);

            Ingredient receivedIngredient = ingredientsDistribution.getIngredient();
            String ingredientName = receivedIngredient.getName();

            if (ingredientsInRecipe.contains(ingredientName)) {
                throw new BadRequestException("Duplicated ingredient: " + ingredientName);
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

    private static void setSteps(Recipe oldRecipe, Recipe newRecipe) {
        List<Step> oldSteps = oldRecipe.getSteps();
        List<Step> newSteps = newRecipe.getSteps();
        oldSteps.sort(Comparator.comparingInt(Step::getStepNum));
        newSteps.sort(Comparator.comparingInt(Step::getStepNum));

        // rest of the oldSteps will be deleted automatically because of orphanRemoval = true
        for (int i = 0; i < newSteps.size(); ++i) {
            Step newStep = newSteps.get(i);
            if (i < oldSteps.size()) {
                newStep.setId(oldSteps.get(i).getId());
            }
            newStep.setRecipe(newRecipe);
        }
    }

    @Override
    public ResponseEntity<List<RecipeDto>> searchRecipesByName(String name, Integer limit, Integer page) {
        List<Recipe> recipes = recipeRepository.findByNameContaining(name, GetUtil.getCurrentLimit(limit), GetUtil.getCurrentPage(page));
        List<RecipeDto> recipeDtos = recipes.stream().map(recipe -> mapper.map(recipe, RecipeDto.class)).toList();
        return ResponseEntity.ok(recipeDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteRecipe(Long recipeId) throws NotFoundException {
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        if (AuthServiceCommon.checkAuthorities(recipe.getAuthor().getUid())) {
            recipeRepository.deleteById(recipeId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecommendations(Integer limit, Integer page) throws NotFoundException {
        SlopeOne recommendAlgSlopeOne = new SlopeOne(mapper, userRepository, markRepository, recipeRepository);
        List<RecipeDto> recipes = recommendAlgSlopeOne.recommendAlgSlopeOne(limit, page);
        return ResponseEntity.ok(recipes);
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getCategoriesByRecipeId(Long id) {
        List<Category> categories = categoryRepository.findByRecipeId(id);
        List<CategoryDto> categoryDtos = categories.stream().map(
                element -> mapper.map(element, CategoryDto.class)).toList();

        return ResponseEntity.ok(categoryDtos);
    }
}
