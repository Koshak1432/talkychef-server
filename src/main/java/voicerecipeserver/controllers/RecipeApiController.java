package voicerecipeserver.controllers;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.api.RecipeApi;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.IngredientsDistributionDto;
import voicerecipeserver.model.dto.RecipeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.StepDto;
import voicerecipeserver.model.entities.*;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.model.mappers.DefaultMapper;
import voicerecipeserver.respository.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//todo многопоточность
  
@RestController
public class RecipeApiController implements RecipeApi {


    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final MeasureUnitRepository measureUnitRepository;
    private final IngredientsDistributionRepository ingredientsDistributionRepository;
    private final StepRepository stepRepository;

    private final DefaultMapper mapper;

    @Autowired
    public RecipeApiController(StepRepository stepRepository,RecipeRepository recipeRepository,IngredientRepository ingredientRepository, MeasureUnitRepository measureUnitRepository,IngredientsDistributionRepository ingredientsDistributionRepository, DefaultMapper mapper){
        this.stepRepository = stepRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.measureUnitRepository = measureUnitRepository;
        this.ingredientsDistributionRepository = ingredientsDistributionRepository;
        this.mapper = mapper;
    }

    //TODO категории в рецепты добавить
//TODO удалить тест
    @PostMapping(value = "test/", consumes = "application/json")
    public ResponseEntity<RecipeDto> test(@Valid @RequestBody RecipeDto recipeDto){


        Recipe recipe = mapper.map(recipeDto, Recipe.class);
        RecipeDto testt = mapper.map(recipe, RecipeDto.class);

        return new ResponseEntity<>(testt,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RecipeDto> recipeIdGet(@javax.validation.constraints.PositiveOrZero Long id) throws NotFoundException {
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
    public ResponseEntity<IdDto> recipePost(RecipeDto recipeDto) throws NotFoundException {
        Recipe recipe = mapper.map(recipeDto, Recipe.class);

        for(Step step : recipe.getSteps()){
            step.setRecipe(recipe);
        }
        for(IngredientsDistribution ingredientsDistribution : recipe.getIngredientsDistributions()){
            ingredientsDistribution.setRecipe(recipe);
            System.out.println(ingredientsDistribution.getIngredient().getName());
            //TODO по имени или ID искать?
            ingredientsDistribution.setIngredient(ingredientRepository.findByName(ingredientsDistribution.getIngredient().getName()).get());
//TODO сука видите ли неотслеживаемый ингредиент я ему кидаю, гандон, просто пидорас. Почему-то каскад из Recipe на distribution переходит рекурсивно на его поля
            //TODO проверять unit на наличие в наборе.
            ingredientsDistribution.setUnit(measureUnitRepository.findByName(ingredientsDistribution.getUnit().getName()).get());
        }

        recipeRepository.save(recipe);

        return new ResponseEntity<>(new IdDto().id(recipe.getId()), HttpStatus.OK);

//        return implTest(recipeDto);
    }


    public ResponseEntity<List<RecipeDto>> recipeSearchNameGet(String name){
        List<Recipe> recipes = recipeRepository.findFirst10ByNameContainingIgnoreCase(name);

        List<RecipeDto> recipeDtos = mapper.map(recipes, new TypeToken<List<RecipeDto>>() {}.getType());


        return new ResponseEntity<>(recipeDtos,HttpStatus.OK);
    }

}
