package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.RecipeApi;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.RecipeService;

import java.util.List;

//TODO категории в рецепты добавить

@CrossOrigin(maxAge = 1440)
@RestController
public class RecipeApiController implements RecipeApi {
    private final RecipeService recipeService;

    @Autowired
    public RecipeApiController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @Override
    public ResponseEntity<RecipeDto> recipeIdGet(Long id) throws NotFoundException, AuthException {
        return recipeService.getRecipeById(id);
    }

    @Override
    public ResponseEntity<IdDto> recipePost(RecipeDto recipeDto) throws NotFoundException, BadRequestException, AuthException {
        return recipeService.addRecipe(recipeDto);
    }

    @Override
    public ResponseEntity<IdDto> recipeUpdate(RecipeDto recipeDto) throws NotFoundException, BadRequestException, AuthException {
        return recipeService.updateRecipe(recipeDto);
    }

    @Override
    public ResponseEntity<Void> recipeDelete(Long id) throws NotFoundException {
        return recipeService.deleteRecipe(id);
    }

    public ResponseEntity<List<RecipeDto>> recipeSearchNameGet(String name, Integer limit) throws NotFoundException, AuthException {
        return recipeService.searchRecipesByName(name, limit);
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getCategories(Long id) throws NotFoundException, BadRequestException {
        return recipeService.getCategoriesById(id);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecipesRecommendations(Integer limit, Integer page) throws NotFoundException, AuthException {
        return recipeService.filterContent(limit, page);
    }


}
