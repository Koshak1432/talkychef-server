package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public interface CollectionService {
    ResponseEntity<Void> addCollection(String name);

    ResponseEntity<Void> addRecipeToCollection(Long recipe,String collection) throws NotFoundException;

    ResponseEntity<CollectionDto> getCollectionByName(String name) throws NotFoundException;
}
