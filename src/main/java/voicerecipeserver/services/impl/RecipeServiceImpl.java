package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
    private final ModelMapper mapper;

    // TODO не ну это колбасу точно убрать надо, мб по-другому внедрить
    @Autowired
    public RecipeServiceImpl(StepRepository stepRepository, RecipeRepository recipeRepository,
                             IngredientRepository ingredientRepository, MeasureUnitRepository measureUnitRepository,
                             IngredientsDistributionRepository ingredientsDistributionRepository, MediaRepository mediaRepository,
                             ModelMapper mapper) {
        this.stepRepository = stepRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.measureUnitRepository = measureUnitRepository;
        this.ingredientsDistributionRepository = ingredientsDistributionRepository;
        this.mediaRepository = mediaRepository;
        this.mapper = mapper;
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
                receivedIngredient.setId(null);
                ingredientRepository.save(receivedIngredient);
            } else {
                ingredientsDistribution.setIngredient(ingredientFromRepo.get());
            }
            //TODO в идеале бы настроить для recipe save так, чтобы он сохранял measureUnit, ingredient, если их нет
            // в БД. Хочется убрать лишний find.

            setMeasureUnit(ingredientsDistribution);
        }
    }

    private void setMeasureUnit(IngredientsDistribution ingredientsDistribution) {
        Optional<MeasureUnit> measureUnitOptional = measureUnitRepository.findByName(ingredientsDistribution.getUnit().getName());
        if (measureUnitOptional.isEmpty()) {
            ingredientsDistribution.setUnit(measureUnitRepository.save(ingredientsDistribution.getUnit()));
        } else {
            ingredientsDistribution.setUnit(measureUnitOptional.get());
        }
    }

    @Override
    public ResponseEntity<IdDto> updateRecipe(RecipeDto recipeDto, Long id) throws NotFoundException, BadRequestException {
        int defaultMediaId = 172;
        Recipe oldRecipe = findRecipe(id); // если обновлять старый и его сохранять, а не новый, мб заработает
        Recipe newRecipe = mapper.map(recipeDto, Recipe.class);
//        newRecipe.getAuthor().setId(oldRecipe.getAuthor().getId());
        setAuthorTo(newRecipe);

        // iterate over old steps and map new step to old step??
        // or delete steps
        List<Step> oldRecipes = oldRecipe.getSteps();
        // reuse of old steps
        for (int i = 0; i < Math.min(oldRecipe.getSteps().size(), newRecipe.getSteps().size()); ++i) {
            newRecipe.getSteps().get(i).setId(oldRecipes.get(i).getId());
        }

        for (Step step : newRecipe.getSteps()) {
            step.setRecipe(newRecipe);
        }

        setDistribution(newRecipe);

        Set<Long> oldRecipeMedia = getRecipeMedia(oldRecipe);
        Set<Long> newRecipeMedia = getRecipeMedia(newRecipe);
        // media to delete
        Set<Long> notUsedMedia = new HashSet<>();
        for (Long mediaId : oldRecipeMedia) {
            if (!newRecipeMedia.contains(mediaId) && mediaId != defaultMediaId) {
                notUsedMedia.add(mediaId);
            }
        }

        recipeRepository.save(newRecipe);
        mediaRepository.deleteAllById(notUsedMedia);
        return new ResponseEntity<>(new IdDto().id(newRecipe.getId()), HttpStatus.OK);
    }

    private Set<Long> getRecipeMedia(Recipe recipe) {
        Set<Long> recipeMedia = new HashSet<>();
        recipeMedia.add(recipe.getMedia().getId());
        for (Step step : recipe.getSteps()) {
            recipeMedia.add(step.getMedia().getId());
        }
        return recipeMedia;
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
}
