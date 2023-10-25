package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.CategoryApi;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.CategoryService;
import voicerecipeserver.services.CollectionService;

import java.util.List;
@RestController
@CrossOrigin(maxAge = 1440)
public class CategoryApiController implements CategoryApi {
    private final CategoryService service;

    @Autowired
    public CategoryApiController(CategoryService service) {
        this.service = service;
    }


    @Override
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return service.getCategories();
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getCategoryRecipes(Long id, Integer limit, Integer page)  {
       return service.getRecipesFromCategory(id, limit, page);
    }

    @Override
    public ResponseEntity<Void> deleteRecipeFromCategory(Long id, Long recipeId) {
        return service.deleteRecipesFromCategory(id, recipeId);
    }

    @Override
    public ResponseEntity<Void> addCategoryToRecipe(Long id, Long categoryId) {
        return service.addCategoryToRecipe(id, categoryId);
    }
}
