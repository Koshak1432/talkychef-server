package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

public interface CategoryService {
    ResponseEntity<List<CategoryDto>> getCategories();

    ResponseEntity<List<RecipeDto>> getRecipesFromCategory(Long id, Integer limit, Integer page);

    ResponseEntity<Void> deleteRecipeFromCategory(Long id, Long recipeId) throws NotFoundException, AuthException;

    ResponseEntity<Void> addCategoryToRecipe(Long id, Long categoryId) throws AuthException, NotFoundException;
}
