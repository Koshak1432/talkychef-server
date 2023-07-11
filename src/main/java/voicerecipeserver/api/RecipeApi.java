package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CommentDto;
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

    @GetMapping(value = "/recipes/{id}", produces = "application/json")
    ResponseEntity<RecipeDto> recipeIdGet(
            @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id) throws
            NotFoundException;

    @PostMapping(value = "/recipes", consumes = "application/json")
    ResponseEntity<IdDto> recipePost(@RequestBody RecipeDto recipeDto) throws NotFoundException, BadRequestException;

    @PutMapping(value = "/recipes", consumes = "application/json")
    ResponseEntity<IdDto> recipeUpdate(@RequestBody RecipeDto recipeDto) throws NotFoundException, BadRequestException;

    @DeleteMapping(value = "/recipe/{id}", produces = "application/json")
    ResponseEntity<Void> recipeDelete(
            @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id) throws
            NotFoundException, BadRequestException;

    @GetMapping(value = "/recipe/search/{name}", produces = "application/json")
    ResponseEntity<List<RecipeDto>> recipeSearchNameGet(
            @Size(max = 128) @NotBlank(message = "name must be not blank") @PathVariable("name") String name,
            @RequestParam(value = "limit", required = false) @Positive(message = "limit must be positive") Integer limit) throws
            NotFoundException;

    @PostMapping(value = "/marks", produces = {"application/json"}, consumes = {"application/json"})
    ResponseEntity<IdDto> markPost(@RequestBody MarkDto mark) throws BadRequestException, NotFoundException;

    @PutMapping(value = "/marks", produces = {"application/json"}, consumes = {"application/json"})
    ResponseEntity<IdDto> markUpdate(@RequestBody MarkDto mark) throws BadRequestException, NotFoundException;

    @DeleteMapping(value = "/marks/{id}", produces = {"application/json"})
    ResponseEntity<Void> markDelete(@PathVariable("id") Long id);

    @PostMapping(value = "/comments", consumes = "application/json")
    ResponseEntity<IdDto> commentPost(@RequestBody CommentDto commentDto) throws NotFoundException, BadRequestException;

    // TODO мб следует ещё один эксепшн с другим кодом создать, чтобы различать not found recipe и not found comment
    // или же просто говорить что всё ок(плохо)

    @PutMapping(value = "/comments", consumes = "application/json")
    ResponseEntity<IdDto> commentUpdate(@RequestBody CommentDto commentDto) throws NotFoundException,
            BadRequestException;

    @DeleteMapping(value = "/comments/{id}")
    ResponseEntity<Void> commentDelete(
            @PathVariable("id") @PositiveOrZero(message = "comment id must be not negative") Long id);
}
