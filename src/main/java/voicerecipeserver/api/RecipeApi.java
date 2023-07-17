package voicerecipeserver.api;

import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

@RequestMapping(Constants.BASE_API_PATH + "/recipes")
@Validated
public interface RecipeApi {

    @GetMapping(value = "/{id}")
    ResponseEntity<RecipeDto> recipeIdGet(
            @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id) throws
            NotFoundException;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PostMapping
    ResponseEntity<IdDto> recipePost(@RequestBody RecipeDto recipeDto) throws NotFoundException, BadRequestException;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PutMapping
    ResponseEntity<IdDto> recipeUpdate(@RequestBody RecipeDto recipeDto) throws NotFoundException, BadRequestException;

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> recipeDelete(
            @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id) throws
            NotFoundException, BadRequestException;

    @GetMapping(value = "/search/{name}")
    ResponseEntity<List<RecipeDto>> recipeSearchNameGet(
            @Size(max = 128) @NotBlank(message = "name must be not blank") @PathVariable("name") String name,
            @RequestParam(value = "limit", required = false) @Positive(message = "limit must be positive") Integer limit) throws
            NotFoundException;
}
