package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.IngredientDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

public interface IngredientService {
    ResponseEntity<IdDto> addIngredient(IngredientDto body) ;

    ResponseEntity<IngredientDto> getIngredientById(Long id) throws NotFoundException;

    ResponseEntity<List<IngredientDto>> searchIngredientsByName(String name);
}
