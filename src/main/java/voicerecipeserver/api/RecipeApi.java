package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Valid
@RequestMapping(Constants.BASE_API_PATH)
public interface RecipeApi {

    @GetMapping(value = "/recipe/{id}", produces = "application/json")
    ResponseEntity<RecipeDto> recipeIdGet(@PathVariable("id") @PositiveOrZero Long id) throws NotFoundException;

    @PostMapping(value = "/recipe", consumes = "application/json")
    ResponseEntity<IdDto> recipePost(@Valid @RequestBody RecipeDto recipeDto) throws NotFoundException, BadRequestException;

    @GetMapping(value = "/recipe/search/{name}", produces = "application/json")
    ResponseEntity<List<RecipeDto>> recipeSearchNameGet(@Size(max=128) @PathVariable("name") String name) throws NotFoundException;
}
