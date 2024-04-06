package talkychefserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import talkychefserver.api.SelectionApi;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.SelectionDto;
import talkychefserver.services.interfaces.SelectionService;

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
    public ResponseEntity<List<CategoryDto>> getCategoriesBySelectionId(Long id) {
        return service.getCategoriesOfSelection(id);
    }
}
