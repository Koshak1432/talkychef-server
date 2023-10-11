package voicerecipeserver.services;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.SelectionDto;

import java.util.List;

public interface SelectionService {

    ResponseEntity<List<SelectionDto>> getAllSelections();
    ResponseEntity<List<CategoryDto>> getCategoriesOfSelection(Long id);

}
