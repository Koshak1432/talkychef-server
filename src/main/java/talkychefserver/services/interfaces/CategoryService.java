package talkychefserver.services.interfaces;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.RecipeDto;

import java.util.List;

public interface CategoryService {
    ResponseEntity<List<CategoryDto>> getCategories();

    ResponseEntity<List<RecipeDto>> getRecipesFromCategory(Long id, Integer limit, Integer page);

    ResponseEntity<Void> deleteRecipeFromCategory(Long categoryId, Long recipeId);

    ResponseEntity<Void> addCategoryToRecipe(Long recipeId, Long categoryId);
}
