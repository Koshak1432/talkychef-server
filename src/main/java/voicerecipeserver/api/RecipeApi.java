package voicerecipeserver.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

@RequestMapping(Constants.BASE_API_PATH + "/recipes")
@Validated
public interface RecipeApi {

    @GetMapping(value = "/{id}")
    ResponseEntity<RecipeDto> recipeIdGet(
            @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id) throws
            NotFoundException, AuthException;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PostMapping
    ResponseEntity<IdDto> recipePost(@RequestBody RecipeDto recipeDto) throws NotFoundException, BadRequestException,
            AuthException;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PutMapping
    ResponseEntity<IdDto> recipeUpdate(@RequestBody RecipeDto recipeDto) throws NotFoundException, BadRequestException,
            AuthException;


    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<Void> recipeDelete(
            @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id) throws
            NotFoundException, BadRequestException;

    @GetMapping(value = "/search/{name}")
    ResponseEntity<List<RecipeDto>> recipeSearchNameGet(
            @Size(max = 128) @NotBlank(message = "name must be not blank") @PathVariable("name") String name,
            @RequestParam(value = "limit", required = false) @Positive(message = "limit must be positive") Integer limit) throws
            NotFoundException, AuthException;

    @GetMapping(value = "/{id}/categories")
    ResponseEntity<List<CategoryDto>> getCategories(
            @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id) throws
            NotFoundException, BadRequestException;


    @GetMapping
    ResponseEntity<List<RecipeDto>> getRecipesRecommendations(
            @RequestParam(value = "limit", required = false) @Positive(message = "limit must be positive") Integer limit,
            @RequestParam(value = "page", required = false) @PositiveOrZero Integer page) throws NotFoundException,
            AuthException;
}
