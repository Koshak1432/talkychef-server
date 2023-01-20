package voicerecipeserver.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.mappers.DefaultMapper;

import javax.validation.Valid;

@RestController
@CrossOrigin(maxAge = 1440)
public class TestController {
    private final ModelMapper mapper;

    @Autowired
    public TestController(DefaultMapper mapper){
        this.mapper = mapper;
    }

    //TODO удалить тест (но пока пусть будет, маппер тестить)
    @PostMapping(value = "test/map/recipe", consumes = "application/json")
    public ResponseEntity<RecipeDto> test(@Valid @RequestBody RecipeDto recipeDto){


        Recipe recipe = mapper.map(recipeDto, Recipe.class);
        RecipeDto testt = mapper.map(recipe, RecipeDto.class);

        return new ResponseEntity<>(testt, HttpStatus.OK);
    }

}
