package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.RecipeApi;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.RecipeService;

import java.util.List;

//TODO категории в рецепты добавить
  
@RestController
@CrossOrigin(maxAge = 1440)
public class RecipeApiController implements RecipeApi {


    private final RecipeService service;


    @Autowired
    public RecipeApiController(RecipeService service){
        this.service = service;
    }


    @Override
    public ResponseEntity<RecipeDto> recipeIdGet(Long id) throws NotFoundException {
        return service.getRecipeById(id);
    }

    @Override
    public ResponseEntity<IdDto> recipePost(RecipeDto recipeDto) throws NotFoundException {
        return service.addRecipe(recipeDto);
    }

    public ResponseEntity<List<RecipeDto>> recipeSearchNameGet(String name) throws NotFoundException{
        return service.searchRecipesByName(name);
    }

}
