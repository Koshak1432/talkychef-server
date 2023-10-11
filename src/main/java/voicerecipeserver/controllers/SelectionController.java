package voicerecipeserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.SelectionApi;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.SelectionDto;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.SelectionService;

import java.util.List;

@CrossOrigin(maxAge = 1440)
@RestController
public class SelectionController implements SelectionApi {
    private final SelectionService service;

    public SelectionController(SelectionService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<SelectionDto>> getAllSelections() {
        return service.getAllSelections();
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getCategoriesBySelectionId(Long id) throws NotFoundException {
        return service.getCategoriesOfSelection(id);
    }
}
