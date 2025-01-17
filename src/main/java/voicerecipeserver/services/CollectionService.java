package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

public interface CollectionService {
    ResponseEntity<IdDto> addCollection(CollectionDto body) throws NotFoundException;

    ResponseEntity<Void> addRecipeToCollection(Long recipe, Long collectionId) throws NotFoundException, AuthException;

    ResponseEntity<CollectionDto> getCollectionById(Long collectionId) throws NotFoundException, AuthException;

    ResponseEntity<Void> deleteCollection(Long id) throws NotFoundException, AuthException;

    ResponseEntity<IdDto> putCollection(Long id, CollectionDto body) throws AuthException, NotFoundException;

    ResponseEntity<Void> deleteRecipeFromCollection(Long recipe, Long collectionId) throws NotFoundException,
            AuthException;

    ResponseEntity<List<CollectionDto>> getCollections(String login, Integer limit, Integer page) throws NotFoundException;

    ResponseEntity<List<CollectionDto>> getCollectionsByName(String name, Integer limit, Integer page) throws NotFoundException;

    ResponseEntity<List<RecipeDto>> getCollectionRecipesById(Long id, Integer limit, Integer page) throws NotFoundException;

    ResponseEntity<IdDto> postLikedRecipe(Long recipeId) throws NotFoundException;
}
