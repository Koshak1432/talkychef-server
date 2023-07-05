package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarksDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.Valid;
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
    ResponseEntity<IdDto> recipeIdMarkPost(@RequestBody MarksDto mark) throws BadRequestException, NotFoundException;

    @GetMapping(value = "/recipe/search/marks",
            produces = {"application/json"})
    ResponseEntity<List<RecipeDto>> recipeSearchMarksGet(@RequestParam(value = "limit", required = false) Integer limit);

    @PutMapping(value = "/recipe/{id}/mark/{mark_id}", produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<MarksDto> recipeIdMarkPut(@RequestBody MarksDto mark, @PathVariable("mark_id") @PositiveOrZero(message = "mark id must be not negative") Long id) throws BadRequestException;




}
