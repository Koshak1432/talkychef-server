package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Valid
public interface RecipeApi {

    @GetMapping(value = "/api/v1/recipe/{id}", produces = "application/json")
    ResponseEntity<RecipeDto> recipeIdGet(@PathVariable("id") @PositiveOrZero Long id) throws NotFoundException;

    @PostMapping(value = "/api/v1/recipe", consumes = "application/json")
    ResponseEntity<IdDto> recipePost(@Valid @RequestBody RecipeDto recipeDto) throws NotFoundException;

    @GetMapping(value = "/api/v1/recipe/search/{name}", produces = "application/json")
    ResponseEntity<List<RecipeDto>> recipeSearchNameGet(@Size(max=128) @PathVariable("name") String name) throws NotFoundException;
}
