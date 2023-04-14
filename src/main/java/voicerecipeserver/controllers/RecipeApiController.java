package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.RecipeApi;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.RecipeService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

//TODO категории в рецепты добавить

@RestController
@CrossOrigin(maxAge = 1440)
public class RecipeApiController implements RecipeApi {
    private final RecipeService service;

    @Autowired
    public RecipeApiController(RecipeService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<RecipeDto> recipeIdGet(Long id) throws NotFoundException {
        return service.getRecipeById(id);
    }

    @Override
    public ResponseEntity<IdDto> recipePost(RecipeDto recipeDto) throws NotFoundException, BadRequestException {
        return service.addRecipe(recipeDto);
    }

    @Override
    public ResponseEntity<IdDto> recipeUpdate(RecipeDto recipeDto, Long id) throws NotFoundException, BadRequestException {
        return service.updateRecipe(recipeDto, id);
    }

    public ResponseEntity<List<RecipeDto>> recipeSearchNameGet(String name, Integer limit) throws NotFoundException {
        return service.searchRecipesByName(name, limit);
    }
}
