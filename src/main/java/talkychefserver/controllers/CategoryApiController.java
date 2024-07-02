package talkychefserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import talkychefserver.api.CategoryApi;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.RecipeDto;
import talkychefserver.services.interfaces.CategoryService;

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
    public ResponseEntity<List<RecipeDto>> getCategoryRecipes(Long id, Integer limit, Integer page) {
        return service.getRecipesFromCategory(id, limit, page);
    }

    @Override
    public ResponseEntity<Void> deleteRecipeFromCategory(Long categoryId, Long recipeId) {
        return service.deleteRecipeFromCategory(categoryId, recipeId);
    }

    @Override
    public ResponseEntity<Void> addCategoryToRecipe(Long recipeId, Long categoryId) {
        return service.addCategoryToRecipe(recipeId, categoryId);
    }

}
