package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

public interface CollectionService {
    ResponseEntity<IdDto> addCollection(String name) throws NotFoundException;

    ResponseEntity<Void> addRecipeToCollection(Long recipe,Long collectionId) throws NotFoundException, AuthException;

    ResponseEntity<CollectionDto> getCollectionPage(Long collectionId) throws NotFoundException, AuthException;

    ResponseEntity<Void> deleteCollection(Long id) throws NotFoundException, AuthException;

    ResponseEntity<IdDto> putCollection(Long id, String name) throws AuthException, NotFoundException;

    ResponseEntity<Void> deleteRecipeFromCollection(Long recipe, Long collectionId) throws NotFoundException, AuthException;

    ResponseEntity<List<CollectionDto>> getCollection(String login) throws NotFoundException;

    ResponseEntity<List<CollectionDto>> getCollectionPageByName(String name, Long pageNum) throws NotFoundException;

}
