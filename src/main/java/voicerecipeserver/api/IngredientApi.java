package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.IngredientDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Valid
public interface IngredientApi {

     @PostMapping(value = "/api/v1/ingredient", consumes = "application/json")
     ResponseEntity<IdDto> ingredientPost(@RequestBody IngredientDto body) ;

     @GetMapping(value = "/api/v1/ingredient/{id}")
     ResponseEntity<IngredientDto> ingredientIdGet(@PathVariable("id") @PositiveOrZero Long id) throws NotFoundException;

     @GetMapping(value = "/api/v1/ingredient/search/{name}", produces = "application/json")
     ResponseEntity<List<IngredientDto>> ingredientSearchNameGet(@Size(max=64) @PathVariable("name") String name);

}
