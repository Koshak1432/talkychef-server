package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.RecipeDto;

import java.util.List;

public interface CategoryService {
    ResponseEntity<List<CategoryDto>> getCategories();

    ResponseEntity<List<RecipeDto>> getRecipesFromCategory(Long id, Integer limit);

    ResponseEntity<Void> deleteRecipesFromCategory(Long id, Long recipeId);

    ResponseEntity<Void> addCategoryToRecipe(Long id, Long categoryId);
}
