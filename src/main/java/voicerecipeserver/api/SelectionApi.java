package voicerecipeserver.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.SelectionDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

@Valid
@RequestMapping(Constants.BASE_API_PATH + "/selections")
public interface SelectionApi {
    @GetMapping
    ResponseEntity<List<SelectionDto>> getAllSelections() ;

    @GetMapping(value = "/{id}")
    ResponseEntity<List<CategoryDto>> getCategoriesBySelectionId(@PathVariable(value = "id") @Positive Long id) throws
            NotFoundException;
}
