package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.*;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.*;
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.security.service.impl.AuthServiceCommon;
import voicerecipeserver.services.RecipeService;

import java.util.*;

import static voicerecipeserver.security.service.impl.AuthServiceCommon.getAuthInfo;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final ModelMapper mapper;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final MeasureUnitRepository measureUnitRepository;
    private final UserRepository userRepository;
    private final AvgMarkRepository avgMarkRepository;
    private final StepRepository stepRepository;
    private final MarkRepository markRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository,
                             MeasureUnitRepository measureUnitRepository, ModelMapper mapper,
                             AvgMarkRepository avgMarkRepository, StepRepository stepRepository,
                             MarkRepository markRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.measureUnitRepository = measureUnitRepository;
        this.stepRepository = stepRepository;
        this.avgMarkRepository = avgMarkRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.mapper.typeMap(Recipe.class, RecipeDto.class).addMappings(
                m -> m.map(src -> src.getAuthor().getUid(), RecipeDto::setAuthorUid));
        this.markRepository = markRepository;
    }


    @Override
    public ResponseEntity<RecipeDto> getRecipeById(Long id) throws NotFoundException, AuthException {
        Recipe recipe = findRecipe(id);
        setAvgMark(recipe);
        RecipeDto recipeDto = mapper.map(recipe, RecipeDto.class);
        setUserMark(recipeDto);
        return ResponseEntity.ok(recipeDto);
    }

    private Recipe findRecipe(Long id) throws NotFoundException {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти рецепт с id: " + id);
        }
        return recipeOptional.get();
    }

    private void checkRecipeMediaUniqByStep(Step step) throws BadRequestException {
        Optional<Recipe> recipeOptional = recipeRepository.findRecipeByMediaId(step.getMedia().getId());
        if (recipeOptional.isPresent()) {
            throw new BadRequestException("ID медиа должно быть уникальным");
        }
    }

    private void checkStepMediaUniqByRecipe(Recipe recipe) throws BadRequestException {
        Optional<Step> stepOptional = stepRepository.findStepByMediaId(recipe.getMedia().getId());
        if (stepOptional.isPresent()) {
            throw new BadRequestException("ID медиа должно быть уникальным");
        }
    }

    private void checkMediaUniqueness(Recipe recipe) throws BadRequestException {
        checkStepMediaUniqByRecipe(recipe);
        Set<Long> mediaIds = new HashSet<>();
        mediaIds.add(recipe.getMedia().getId());

        for (Step step : recipe.getSteps()) {
            if (step.getMedia() == null) {
                continue;
            }
            checkRecipeMediaUniqByStep(step);
            Long mediaId = step.getMedia().getId();
            if (mediaIds.contains(mediaId)) {
                throw new BadRequestException("ID медиа должно быть уникальным");
            } else {
                mediaIds.add(mediaId);
            }
        }
    }

    @Override
    public ResponseEntity<IdDto> addRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException,
            AuthException {
        if (!AuthServiceCommon.checkAuthorities(recipeDto.getAuthorUid())) {
            throw new AuthException("Нет прав");
        }
        Recipe recipe = mapper.map(recipeDto, Recipe.class);
        setAuthorToRecipe(recipe);
        recipe.setId(null);
        checkMediaUniqueness(recipe);
        System.out.println(recipe.getMedia());
        // через маппер можно сделать путем добавления конвертера. Только вот код
        // там будет хуже, его будет сильно больше, а производительность вряд ли вырастет
        for (Step step : recipe.getSteps()) {
            step.setRecipe(recipe);
        }

        setDistribution(recipe);
        Recipe savedRecipe = recipeRepository.save(recipe);
        return ResponseEntity.ok(new IdDto().id(savedRecipe.getId()));
    }

    private void setAuthorToRecipe(Recipe recipe) throws NotFoundException {
        Optional<User> author = userRepository.findByUid(recipe.getAuthor().getUid());
        if (author.isEmpty()) {
            throw new NotFoundException("Не удалось найти автора с uid: " + recipe.getAuthor().getUid());
        } else {
            recipe.setAuthor(author.get());
        }
    }

    @Override
    public ResponseEntity<IdDto> updateRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException,
            AuthException {
        if (!AuthServiceCommon.checkAuthorities(recipeDto.getAuthorUid())) {
            throw new AuthException("Нет прав");
        }
        Recipe oldRecipe = findRecipe(recipeDto.getId());
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
    public ResponseEntity<List<RecipeDto>> searchRecipesByName(String name, Integer limit) throws NotFoundException, AuthException {
        if (limit == null) {
            limit = 0;
        }
        List<Recipe> recipes = recipeRepository.findByNameContaining(name, limit);
        for (Recipe recipe : recipes) {
            setAvgMark(recipe);
        }

        if (recipes.isEmpty()) {
            throw new NotFoundException("Не удалось найти рецепты с подстрокой: " + name);
        }
        List<RecipeDto> recipeDtos = mapper.map(recipes, new TypeToken<List<RecipeDto>>() {
        }.getType());
        for (RecipeDto recipeDto : recipeDtos) {
            setUserMark(recipeDto);
        }
        return ResponseEntity.ok(recipeDtos);
    }

    private void setAvgMark(Recipe recipe) {
        Optional<AvgMark> avgMarkOptional = avgMarkRepository.findById(recipe.getId());
        avgMarkOptional.ifPresent(recipe::setAvgMark);
    }

    private void setUserMark(RecipeDto recipe) throws AuthException {
        CollectionServiceImpl.setUserMarkToRecipe(recipe, userRepository, markRepository);
    }


    @Override
    public ResponseEntity<Void> deleteRecipe(Long recipeId) throws NotFoundException {
        Recipe recipe = findRecipe(recipeId);
        if (AuthServiceCommon.checkAuthorities(recipe.getAuthor().getUid())) {
            recipeRepository.deleteById(recipeId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
