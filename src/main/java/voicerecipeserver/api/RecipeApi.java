package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CommentDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

//todo add /recipe to RequestMapping ?
@RequestMapping(Constants.BASE_API_PATH)
@Validated
public interface RecipeApi {

    @GetMapping(value = "/recipe/{id}", produces = "application/json")
    ResponseEntity<RecipeDto> recipeIdGet(
            @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id) throws
            NotFoundException;

    @PostMapping(value = "/recipe", consumes = "application/json")
    ResponseEntity<IdDto> recipePost(@RequestBody RecipeDto recipeDto) throws NotFoundException, BadRequestException;

    @PutMapping(value = "/recipe/{id}", consumes = "application/json")
    ResponseEntity<IdDto> recipeUpdate(@RequestBody RecipeDto recipeDto,
                                       @PathVariable("id") @PositiveOrZero(message = "recipe id must be not negative") Long id) throws
            NotFoundException, BadRequestException;

    @GetMapping(value = "/recipe/search/{name}", produces = "application/json")
    ResponseEntity<List<RecipeDto>> recipeSearchNameGet(
            @Size(max = 128) @NotBlank(message = "name must be not blank") @PathVariable("name") String name,
            @RequestParam(value = "limit", required = false) @Positive(message = "limit must be positive") Integer limit) throws
            NotFoundException;


    @PostMapping(value = "/recipe/{id}/comment", consumes = "application/json")
    ResponseEntity<IdDto> commentPost(@RequestBody CommentDto commentDto) throws NotFoundException, BadRequestException;

    // TODO мб следует ещё один эксепшн с другим кодом создать, чтобы различать not found recipe и not found comment
    // или же просто говорить что всё ок(плохо)

    @PutMapping(value = "/recipe/{id}/comment", consumes = "application/json")
    ResponseEntity<IdDto> commentUpdate(@RequestBody CommentDto commentDto) throws NotFoundException,
            BadRequestException;

    @DeleteMapping(value = "/recipe/{recipe_id}/comment/{comment_id}")
    ResponseEntity<Void> commentDelete(@PathVariable("comment_id") @PositiveOrZero(message = "comment id must be not negative") Long commentId);
}
