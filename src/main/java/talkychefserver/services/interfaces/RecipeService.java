package talkychefserver.services.interfaces;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.RecipeDto;

import java.util.List;

public interface RecipeService {
    ResponseEntity<RecipeDto> getRecipeById(Long id);

    ResponseEntity<IdDto> addRecipe(RecipeDto recipeDto);

    ResponseEntity<IdDto> updateRecipe(RecipeDto recipeDto);

    ResponseEntity<List<RecipeDto>> searchRecipesByName(String name, Integer limit, Integer page);

    ResponseEntity<Void> deleteRecipe(Long id);

    ResponseEntity<List<RecipeDto>> getRecommendations(Integer limit, Integer page);

    ResponseEntity<List<CategoryDto>> getCategoriesByRecipeId(Long id);
}
