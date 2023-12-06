package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.CategoryApi;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.security.service.impl.AuthServiceCommon;
import voicerecipeserver.services.CategoryService;

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
    public ResponseEntity<Void> deleteRecipeFromCategory(Long categoryId, Long recipeId) throws NotFoundException,
            AuthException {
        return service.deleteRecipeFromCategory(categoryId, recipeId);
    }

    @Override
    public ResponseEntity<Void> addCategoryToRecipe(Long recipeId, Long categoryId) throws AuthException,
            NotFoundException {
        return service.addCategoryToRecipe(recipeId, categoryId);
    }

}
