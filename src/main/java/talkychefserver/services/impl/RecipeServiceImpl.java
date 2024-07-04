package talkychefserver.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.RecipeDto;
import talkychefserver.model.entities.Collection;
import talkychefserver.model.entities.*;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;
import talkychefserver.recommend.SlopeOne;
import talkychefserver.respositories.*;
import talkychefserver.security.service.impl.AuthServiceCommon;
import talkychefserver.services.interfaces.RecipeService;
import talkychefserver.utils.FindUtils;
import talkychefserver.utils.GetUtil;

import java.util.*;

@Slf4j
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
    private final ProductRepository productRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository,
                             MeasureUnitRepository measureUnitRepository, ModelMapper mapper,
                             AvgMarkRepository avgMarkRepository, StepRepository stepRepository,
                             MarkRepository markRepository, UserRepository userRepository,
                             MediaRepository mediaRepository, CollectionRepository collectionRepository,
                             CategoryRepository categoryRepository, ProductRepository productRepository, ProductRepository productRepository1) {

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
        this.productRepository = productRepository1;
    }


    @Override
    public ResponseEntity<RecipeDto> getRecipeById(Long id) {
        log.info("Processing get recipe by id [{}] request", id);
        Recipe recipe = FindUtils.findRecipe(recipeRepository, id);
        RecipeDto recipeDto = mapper.map(recipe, RecipeDto.class);
        log.info("Processed get recipe by id request");
        return ResponseEntity.ok(recipeDto);
    }

    private boolean isRecipeMediaUniqByStep(Long stepMediaId) {
        return recipeRepository.findRecipeByMediaId(stepMediaId).isEmpty();
    }

    private boolean isStepMediaUniqByRecipe(Long recipeMediaId) {
        return stepRepository.findStepByMediaId(recipeMediaId).isEmpty();
    }

    private boolean isRecipeMediaUniq(Long recipeMediaId) {
        return recipeRepository.findRecipeByMediaId(recipeMediaId).isEmpty();
    }

    private void checkMediaUniqueness(Recipe recipe) {
        log.info("Checking media uniqueness");
        Long recipeMediaId = recipe.getMedia().getId();
        if (!(isStepMediaUniqByRecipe(recipeMediaId) && isRecipeMediaUniq(recipeMediaId))) {
            log.error("Media id [{}] already exists, must be unique", recipeMediaId);
            throw new BadRequestException("Media id must be unique");
        }
        Set<Long> mediaIds = new HashSet<>();
        mediaIds.add(recipeMediaId);

        for (Step step : recipe.getSteps()) {
            if (step.getMedia() == null) {
                continue;
            }
            Long stepMediaId = step.getMedia().getId();
            if (!isRecipeMediaUniqByStep(stepMediaId)) {
                log.error("Media id [{}] already exists, must be unique", stepMediaId);
                throw new BadRequestException("Media id must be unique");
            }

            if (mediaIds.contains(stepMediaId)) {
                log.error("Media id [{}] isn't unique within the recipe", stepMediaId);
                throw new BadRequestException("Media id " + stepMediaId + " must be unique within the recipe");
            }
            mediaIds.add(stepMediaId);
        }
        log.info("Media uniqueness checked");
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> addRecipe(RecipeDto recipeDto) {
        log.info("Processing add recipe request");
        if (!AuthServiceCommon.checkAuthorities(recipeDto.getAuthorUid())) {
            log.error("User has no rights to add recipe");
            throw new AuthException("No rights");
        }
        if (recipeDto.getMediaId() == null) {
            log.error("Recipe mediaId is null in DTO");
            throw new BadRequestException("Media id must be present");
        }
        Recipe recipe = mapper.map(recipeDto, Recipe.class);
        Long recipeMediaId = recipe.getMedia().getId();
        if (mediaRepository.findById(recipeMediaId).isEmpty()) {
            log.error("Couldn't find recipe media [{}]", recipeMediaId);
            throw new NotFoundException("Couldn't find recipe media with id: " + recipeMediaId);
        }
        User author = FindUtils.findUserByUid(userRepository, recipe.getAuthor().getUid());
        recipe.setAuthor(author);
        recipe.setId(null);
        checkMediaUniqueness(recipe);
        // через маппер можно сделать путем добавления конвертера. Только вот код
        // там будет хуже, его будет сильно больше, а производительность вряд ли вырастет
        for (Step step : recipe.getSteps()) {
            step.setRecipe(recipe);
        }
        setDistribution(recipe);
        Recipe savedRecipe = recipeRepository.save(recipe);

        addRecipeToSavedCollection(author, savedRecipe);
        log.info("Added recipe with id [{}], author: [{}]", savedRecipe.getId(), author.getUid());
        return ResponseEntity.ok(new IdDto().id(savedRecipe.getId()));
    }

    private void addRecipeToSavedCollection(User author, Recipe savedRecipe) {
        log.info("Adding recipe to saved(own) collection");
        String savedName = author.getUid() + "_saved";
        Collection saveCollection = collectionRepository.findByAuthorIdUserRecipeCollection(author.getId(),
                                                                                            savedName).orElse(null);
        if (saveCollection == null) {
            saveCollection = collectionRepository.save(new Collection(savedName, 0, author));
        }
        collectionRepository.addRecipeToCollection(savedRecipe.getId(), saveCollection.getId());
        log.info("Added recipe to saved collection");
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> updateRecipe(RecipeDto recipeDto) {
        log.info("Processing update recipe request");
        if (!AuthServiceCommon.checkAuthorities(recipeDto.getAuthorUid())) {
            log.error("User has no rights to update recipe");
            throw new AuthException("No rights");
        }
        Recipe oldRecipe = FindUtils.findRecipe(recipeRepository, recipeDto.getId());
        Recipe newRecipe = mapper.map(recipeDto, Recipe.class);

        newRecipe.setId(recipeDto.getId());
        User author = FindUtils.findUserByUid(userRepository, newRecipe.getAuthor().getUid());
        newRecipe.setAuthor(author);
        setSteps(oldRecipe, newRecipe);
        checkMediaUniqueness(newRecipe);
        setDistribution(newRecipe);
        Recipe savedRecipe = recipeRepository.save(newRecipe);
        log.info("Updated recipe [{}]", savedRecipe.getId());
        return ResponseEntity.ok(new IdDto().id(newRecipe.getId()));
    }

    private void setDistribution(Recipe recipe) {
        log.info("Setting ingredients distribution");
        HashSet<String> ingredientsInRecipe = new HashSet<>();
        for (IngredientsDistribution ingredientsDistribution : recipe.getIngredientsDistributions()) {
            ingredientsDistribution.setRecipe(recipe);

            Ingredient receivedIngredient = ingredientsDistribution.getIngredient();
            String ingredientName = receivedIngredient.getName();

            if (ingredientsInRecipe.contains(ingredientName)) {
                log.error("Ingredient [{}] is duplicated", ingredientName);
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

        log.info("Set ingredients distribution");
        setNutritionParameters( recipe);
    }

    private void setNutritionParameters( Recipe recipe) {
        double kilocalories = 1;
        double proteins = 1;
        double fats = 1;
        double carbohydrates = 1;
        double totalWeight = 1;
        double ingredientWeight;
        log.info("Setting nutrition distribution");

        for (IngredientsDistribution ingredientsDistribution : recipe.getIngredientsDistributions()) {
            Optional<Product> optionalProduct = productRepository.findById(ingredientsDistribution.getIngredient().getId());
            if (optionalProduct.isEmpty()) continue;
            Product product = optionalProduct.get();
            totalWeight += ingredientsDistribution.getMeasureUnitCount() * ingredientsDistribution.getUnit().getConversionToGrams();
            ingredientWeight = ingredientsDistribution.getMeasureUnitCount() * ingredientsDistribution.getUnit().getConversionToGrams();

            kilocalories += kilocalories + (ingredientWeight / product.getServing() * product.getEnergyValue());
            proteins += proteins + (ingredientWeight / product.getServing() * product.getProtein());
            fats += fats + (ingredientWeight / product.getServing() * product.getFat());
            carbohydrates += carbohydrates + (ingredientWeight / product.getServing() * product.getCarbohydrates());
        }
        if (recipe.getServings()!=null) {
            kilocalories/=recipe.getServings();
            proteins/=recipe.getServings();
            fats/=recipe.getServings();
            carbohydrates/=recipe.getServings();
            totalWeight/=recipe.getServings();
        }
        recipe.setKilocalories(kilocalories);
        recipe.setProteins(proteins);
        recipe.setFats(fats);
        recipe.setCarbohydrates(carbohydrates);
        recipe.setTotalWeight((long) totalWeight);
        log.info("Set nutrition distribution");
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
        log.info("Setting steps to new recipe [{}]", newRecipe.getId());
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
        log.info("Set steps to new recipe [{}]", newRecipe.getId());
    }

    @Override
    public ResponseEntity<List<RecipeDto>> searchRecipesByName(String name, Integer limit, Integer page) {
        log.info("Processing search recipes by name [{}]", name);
        List<Recipe> recipes = recipeRepository.findByNameContaining(name, GetUtil.getCurrentLimit(limit),
                                                                     GetUtil.getCurrentPage(page));
        List<RecipeDto> recipeDtos = recipes.stream().map(recipe -> mapper.map(recipe, RecipeDto.class)).toList();
        log.info("Response recipe list size: {}", recipeDtos.size());
        return ResponseEntity.ok(recipeDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteRecipe(Long recipeId) {
        log.info("Processing delete recipe [{}] request", recipeId);
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        if (!AuthServiceCommon.checkAuthorities(recipe.getAuthor().getUid())) {
            log.error("User has no rights to delete recipe [{}]", recipeId);
            throw new AuthException("Has no rights");
        }
        recipeRepository.deleteById(recipeId);
        log.info("Recipe [{}] deleted", recipeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecommendations(Integer limit, Integer page) {
        log.info("Processing get recommendations request");
        SlopeOne recommendAlgSlopeOne = new SlopeOne(mapper, userRepository, markRepository, recipeRepository);
        List<RecipeDto> recipeDtos = recommendAlgSlopeOne.recommendAlgSlopeOne(limit, page);
        log.info("Response recipe list size: {}", recipeDtos.size());
        return ResponseEntity.ok(recipeDtos);
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getCategoriesByRecipeId(Long id) {
        log.info("Processing get categories by recipe id [{}] request", id);
        List<Category> categories = categoryRepository.findByRecipeId(id);
        List<CategoryDto> categoryDtos = categories.stream().map(
                element -> mapper.map(element, CategoryDto.class)).toList();
        log.info("Response category list size: {}", categoryDtos.size());
        return ResponseEntity.ok(categoryDtos);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecipesByIds(List<Long> productIds) {
        List<Recipe> recipes = recipeRepository.findRecipesNotContainingProducts(productIds);
        List<RecipeDto> recipeDtos = recipes.stream().map(recipe -> mapper.map(recipe, RecipeDto.class)).toList();
        return ResponseEntity.ok(recipeDtos);
    }
}
