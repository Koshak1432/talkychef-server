package voicerecipeserver.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.FilterDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.dto.TimeDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.io.IOException;
import java.util.List;

@Valid
@RequestMapping(Constants.BASE_API_PATH + "/helper")
public interface FilterApi {
    @GetMapping
    ResponseEntity<FilterDto> getFilters() throws IOException;

    @GetMapping(value = "/times")
    ResponseEntity<List<TimeDto>> getTimes() throws IOException;
    @PostMapping
    ResponseEntity<List<RecipeDto>> getRecipes(@Valid @RequestBody FilterDto body);

    @PostMapping(value = "/category")
    ResponseEntity<FilterDto> helperPostCategory(@RequestParam(value = "category_id", required = false) Long id) throws
            NotFoundException, IOException;
    @PostMapping(value = "/time")
    ResponseEntity<FilterDto> helperPostTime(@RequestParam(value = "time_id", required = false) Long id) throws
            NotFoundException, IOException;
    @PostMapping(value = "/ingredient")
    ResponseEntity<FilterDto> helperPostIngredient(@RequestParam(value = "ingredient_id", required = false) Long id) throws
            NotFoundException, IOException;

    @DeleteMapping(value = "/category")
    ResponseEntity<FilterDto> helperDeleteCategory(@RequestParam(value = "category_id", required = false) Long id) throws
            NotFoundException, IOException;
    @DeleteMapping(value = "/time")
    ResponseEntity<FilterDto> helperDeleteTime(@RequestParam(value = "time_id", required = false) Long id) throws
            NotFoundException, IOException;
    @DeleteMapping(value = "/ingredient")
    ResponseEntity<FilterDto> helperDeleteIngredient(@RequestParam(value = "ingredient_id", required = false) Long id) throws
            NotFoundException, IOException;
}
