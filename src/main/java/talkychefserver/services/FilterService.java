package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.FilterDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.dto.TimeDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.io.IOException;
import java.util.List;

public interface FilterService {
    ResponseEntity<FilterDto> getFilters() throws IOException;

    ResponseEntity<List<RecipeDto>> getRecipes(FilterDto body);

    ResponseEntity<FilterDto> postCategoryHelper(Long id) throws NotFoundException, IOException;

    ResponseEntity<FilterDto> helperPostTime(Long id) throws IOException, NotFoundException;

    ResponseEntity<FilterDto> helperPostIngredient(Long id) throws IOException, NotFoundException;

    ResponseEntity<FilterDto> helperDeleteCategory(Long id) throws IOException, NotFoundException;

    ResponseEntity<FilterDto> helperDeleteTime(Long id) throws IOException, NotFoundException;

    ResponseEntity<FilterDto> helperDeleteIngredient(Long id) throws IOException, NotFoundException;

    ResponseEntity<List<TimeDto>> getTimes();
}
