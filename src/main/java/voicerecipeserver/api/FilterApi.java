package voicerecipeserver.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.FilterDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

@Valid
@RequestMapping(Constants.BASE_API_PATH + "/helper")
public interface FilterApi {
    @GetMapping
    ResponseEntity<FilterDto> getFilters();
    @PostMapping
    ResponseEntity<List<RecipeDto>> getRecipes(@Valid @RequestBody FilterDto body);

    @GetMapping(value = "/category")
    ResponseEntity<FilterDto> helperPostCategory(@RequestParam(value = "category_id", required = false) Long id)  throws
            NotFoundException;
    @GetMapping(value = "/time")
    ResponseEntity<FilterDto> helperPostTime(@RequestParam(value = "time_id", required = false) Long id)  throws
            NotFoundException;
    @GetMapping(value = "/ingredient")
    ResponseEntity<FilterDto> helperPostIngredient(@RequestParam(value = "ingredient_id", required = false) Long id) throws
            NotFoundException;
}
