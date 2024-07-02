package talkychefserver.services.interfaces;


import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.SelectionDto;

import java.util.List;

public interface SelectionService {

    ResponseEntity<List<SelectionDto>> getAllSelections();

    ResponseEntity<List<CategoryDto>> getCategoriesOfSelection(Long id);

}
