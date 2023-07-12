package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@RequestMapping(Constants.BASE_API_PATH + "/recipes")
@Validated
public interface RecipeApi {

    @GetMapping(value = "/{id}")
    ResponseEntity<RecipeDto> recipeIdGet(
            @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id) throws
            NotFoundException;

    @PostMapping
    ResponseEntity<IdDto> recipePost(@RequestBody RecipeDto recipeDto) throws NotFoundException, BadRequestException;

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
