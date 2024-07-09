package talkychefserver.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import talkychefserver.config.Constants;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.RecipeDto;

import java.util.List;

@RequestMapping(Constants.BASE_API_PATH + "/recipes")
@Validated
public interface RecipeApi {

    @GetMapping(value = "/{id}")
    ResponseEntity<RecipeDto> getRecipeById(
            @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id);

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PostMapping
    ResponseEntity<IdDto> addRecipe(@RequestBody RecipeDto recipeDto);

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PutMapping
    ResponseEntity<IdDto> updateRecipe(@RequestBody RecipeDto recipeDto);

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<Void> deleteRecipe(
            @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id);

    @GetMapping(value = "/search/{name}")
    ResponseEntity<List<RecipeDto>> getRecipesByName(
            @Size(max = 128) @NotBlank(message = "name must be not blank") @PathVariable("name") String name,
            @RequestParam(value = "limit", required = false) @Positive(message = "limit must be positive") Integer limit,
            @RequestParam(value = "page", required = false) @PositiveOrZero Integer page);

    @GetMapping(value = "/{id}/categories")
    ResponseEntity<List<CategoryDto>> getCategoriesByRecipeId(
            @PathVariable("id") @Positive(message = "recipe id must be > 0") Long id);


    @GetMapping
    ResponseEntity<List<RecipeDto>> getRecipesRecommendations(
            @RequestParam(value = "limit", required = false) @Positive Integer limit,
            @RequestParam(value = "page", required = false) @PositiveOrZero Integer page);

    @PostMapping("/find-by-ids")
     ResponseEntity<List<RecipeDto>> findRecipesByIds(@RequestBody List<Long> ids);

}
