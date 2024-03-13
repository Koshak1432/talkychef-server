package talkychefserver.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import talkychefserver.config.Constants;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.RecipeDto;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;

import java.util.List;

@Valid
@RequestMapping(Constants.BASE_API_PATH + "/categories")
public interface CategoryApi {
    @GetMapping
    ResponseEntity<List<CategoryDto>> getAllCategories() throws NotFoundException;

    @GetMapping(value = "/{id}")
    ResponseEntity<List<RecipeDto>> getCategoryRecipes(@PathVariable(value = "id") Long id,
                                                       @RequestParam(value = "limit", required = false) @PositiveOrZero Integer limit,
                                                       @RequestParam(value = "page", required = false) @PositiveOrZero Integer page) throws
            NotFoundException, AuthException, BadRequestException;

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteRecipeFromCategory(@PathVariable(value = "id") Long categoryId,
                                                  @RequestParam(value = "recipe_id") @PositiveOrZero Long recipeId) throws
            NotFoundException, AuthException;

    @PostMapping(value = "/recipes")
    ResponseEntity<Void> addCategoryToRecipe(@RequestParam(value = "recipe_id") Long recipeId,
                                             @RequestParam(value = "category_id") Long categoryId) throws
            NotFoundException, AuthException, BadRequestException;
}
