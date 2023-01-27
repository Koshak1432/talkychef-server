package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.IngredientDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Valid
@RequestMapping(Constants.BASE_API_PATH)
public interface IngredientApi {

//     @PostMapping(value = "/ingredient", consumes = "application/json")
     ResponseEntity<IdDto> ingredientPost(@RequestBody IngredientDto body) ;

//     @GetMapping(value = "/ingredient/{id}")
     ResponseEntity<IngredientDto> ingredientIdGet(@PathVariable("id") @PositiveOrZero Long id) throws NotFoundException;

//     @GetMapping(value = "/ingredient/search/{name}", produces = "application/json")
     ResponseEntity<List<IngredientDto>> ingredientSearchNameGet(@Size(max=64) @PathVariable("name") String name);

}
