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
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.*;
import voicerecipeserver.services.RecipeService;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final MeasureUnitRepository measureUnitRepository;
    private final IngredientsDistributionRepository ingredientsDistributionRepository;
    private final StepRepository stepRepository;

    private final ModelMapper mapper;

    // TODO не ну это колбасу точно убрать надо
    @Autowired
    public RecipeServiceImpl(StepRepository stepRepository,RecipeRepository recipeRepository,IngredientRepository ingredientRepository, MeasureUnitRepository measureUnitRepository,IngredientsDistributionRepository ingredientsDistributionRepository, ModelMapper mapper){
        this.stepRepository = stepRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.measureUnitRepository = measureUnitRepository;
        this.ingredientsDistributionRepository = ingredientsDistributionRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<RecipeDto> getRecipeById(Long id) throws NotFoundException {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if(recipeOptional.isEmpty()){
            throw new NotFoundException("recipe");
        }

        Recipe recipe = recipeOptional.get();
        RecipeDto recipeDto = mapper.map(recipe, RecipeDto.class);
        return new ResponseEntity<>(recipeDto, HttpStatus.OK);
    }

    //todo проверить, является ли это сохранение одной транзакцией. По идее должно, и висячих распределений, шагов не останется.
    @Override
    public ResponseEntity<IdDto> addRecipe(RecipeDto recipeDto) throws NotFoundException {
        Recipe recipe = mapper.map(recipeDto, Recipe.class);

        for(Step step : recipe.getSteps()){
            step.setRecipe(recipe);
        }
        for(IngredientsDistribution ingredientsDistribution : recipe.getIngredientsDistributions()){
            ingredientsDistribution.setRecipe(recipe);
            ingredientsDistribution.setId(new IngredientsDistributionKey());
            ingredientsDistribution.getIngredient().setName(ingredientsDistribution.getIngredient().getName().toLowerCase());

            //TODO по имени или ID искать?
            Optional<Ingredient> ingredientOptional = ingredientRepository.findByName(ingredientsDistribution.getIngredient().getName());
            if(ingredientOptional.isEmpty()){
                ingredientsDistribution.setIngredient(ingredientRepository.save(ingredientsDistribution.getIngredient()));
            } else {
                ingredientsDistribution.setIngredient(ingredientOptional.get());
            }
            //TODO в идеале бы настроить для recipe save так, чтобы он сохранял measureUnit, ingredient, если их нет в БД. Ибо они уже отмаплены и просто запросы гоняем лишние. Тупо.

            Optional<MeasureUnit> measureUnitOptional = measureUnitRepository.findByName(ingredientsDistribution.getUnit().getName());
            if(measureUnitOptional.isEmpty()){
                ingredientsDistribution.setUnit(measureUnitRepository.save(ingredientsDistribution.getUnit()));
            } else {
                ingredientsDistribution.setUnit(measureUnitOptional.get());
            }
        }
        recipeRepository.save(recipe);
        return new ResponseEntity<>(new IdDto().id(recipe.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> searchRecipesByName(String name) throws NotFoundException {
        Optional<List<Recipe>> recipes = recipeRepository.findFirst10ByNameContaining(name);

        if(recipes.isEmpty()){
            throw  new NotFoundException("Can't find recipes with substr: " + name);
        }
        List<RecipeDto> recipeDtos = mapper.map(recipes, new TypeToken<List<RecipeDto>>() {}.getType());


        return new ResponseEntity<>(recipeDtos,HttpStatus.OK);
    }
}
