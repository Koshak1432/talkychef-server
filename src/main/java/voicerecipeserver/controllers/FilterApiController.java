package voicerecipeserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.api.FilterApi;
import voicerecipeserver.model.dto.FilterDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.dto.TimeDto;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.FilterService;

import java.io.IOException;
import java.util.List;


@CrossOrigin(maxAge = 1440)
@RestController
public class FilterApiController implements FilterApi {
    private final FilterService service;

    public FilterApiController(FilterService service) {
        this.service = service;
    }


    @Override
    public ResponseEntity<FilterDto> getFilters() throws IOException {
        return service.getFilters();
    }

    @Override
    public ResponseEntity<List<TimeDto>> getTimes() throws IOException {
        return service.getTimes();
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecipes(FilterDto body) {
        return service.getRecipes(body);
    }

    @Override
    public ResponseEntity<FilterDto> helperPostCategory(Long id) throws NotFoundException, IOException {
        return service.postCategoryHelper(id);
    }

    @Override
    public ResponseEntity<FilterDto> helperPostTime(Long id) throws NotFoundException, IOException {
        return service.helperPostTime(id);
    }

    @Override
    public ResponseEntity<FilterDto> helperPostIngredient(Long id) throws NotFoundException, IOException {
        return service.helperPostIngredient(id);

    }

    @Override
    public ResponseEntity<FilterDto> helperDeleteCategory(Long id) throws NotFoundException, IOException {
        return service.helperDeleteCategory(id);
    }

    @Override
    public ResponseEntity<FilterDto> helperDeleteTime(Long id) throws NotFoundException, IOException {
        return service.helperDeleteTime(id);

    }

    @Override
    public ResponseEntity<FilterDto> helperDeleteIngredient(Long id) throws NotFoundException, IOException {
        return service.helperDeleteIngredient(id);

    }
}
