package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.exceptions.NotFoundException;

public interface CollectionService {
    ResponseEntity<Void> addCollection(String name);

    ResponseEntity<Void> addRecipeToCollection(Long recipe,String collection) throws NotFoundException;

    ResponseEntity<CollectionDto> getCollectionPage(String name, Integer pageNum) throws NotFoundException;
}
