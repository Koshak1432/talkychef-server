package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarkDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.persistence.Id;
import javax.validation.constraints.*;
import java.util.List;

//todo add /recipe to RequestMapping ?
@RequestMapping(Constants.BASE_API_PATH)
@Validated
public interface RecipeApi {

    @GetMapping(value = "/recipe/{id}", produces = "application/json")
    ResponseEntity<RecipeDto> recipeIdGet(@PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id) throws NotFoundException;

    @PostMapping(value = "/recipe", consumes = "application/json")
    ResponseEntity<IdDto> recipePost(@RequestBody RecipeDto recipeDto) throws NotFoundException, BadRequestException;

    @PutMapping(value = "/recipe/{id}", consumes = "application/json")
    ResponseEntity<IdDto> recipeUpdate(@RequestBody RecipeDto recipeDto, @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id)
            throws NotFoundException, BadRequestException;

    @GetMapping(value = "/recipe/search/{name}", produces = "application/json")
    ResponseEntity<List<RecipeDto>> recipeSearchNameGet(
            @Size(max = 128) @NotBlank(message = "name must be not blank") @PathVariable("name") String name,
            @RequestParam(value = "limit", required = false) @Positive(message = "limit must be positive") Integer limit) throws NotFoundException;


    @PostMapping(value = "/recipe/{id}/mark", produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<IdDto> MarkPost(@RequestBody MarkDto mark) throws BadRequestException, NotFoundException;

    @PutMapping(value = "/mark/{id}", produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<IdDto> MarkUpdate(@RequestBody MarkDto mark) throws BadRequestException, NotFoundException;

    @DeleteMapping(value = "/mark/{id}",
            produces = {"application/json"})
    ResponseEntity<Void> MarkDelete(@PathVariable("id") Long id);

}
