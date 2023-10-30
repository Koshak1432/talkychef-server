package voicerecipeserver.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.CollectionApi;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.CollectionService;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 1440)
public class CollectionApiController implements CollectionApi {

    private final CollectionService service;

    @Autowired
    public CollectionApiController(CollectionService service) {
        this.service = service;
    }


    @Override
    public ResponseEntity<List<CollectionDto>> getUserCollections(String login, Integer limit, Integer page) throws NotFoundException {
        return service.getCollections(login, limit, page);
    }

    @Override
    public ResponseEntity<IdDto> addCollection(CollectionDto body) throws NotFoundException {
        return service.addCollection(body);
    }

    @Override
    public ResponseEntity<Void> deleteCollection(Long id) throws NotFoundException, AuthException {
        return service.deleteCollection(id);

    }

    @Override
    public ResponseEntity<IdDto> updateCollection(Long id, CollectionDto body) throws NotFoundException, AuthException {
        return service.putCollection(id, body);

    }

    @Override
    public ResponseEntity<IdDto> addRecipeToLiked(Long recipeId) throws NotFoundException {
        return service.postLikedRecipe(recipeId);
    }

    @Override
    public ResponseEntity<Void> deleteRecipeFromCollection(Long recipe, Long collectionId) throws NotFoundException,
            AuthException {
        return service.deleteRecipeFromCollection(recipe, collectionId);
    }

    @Override
    public ResponseEntity<Void> addRecipeToCollection(Long recipe, Long collectionId) throws NotFoundException,
            AuthException {
        return service.addRecipeToCollection(recipe, collectionId);
    }

    @Override
    public ResponseEntity<CollectionDto> getCollectionById(Long collectionId) throws NotFoundException,
            AuthException {
        return service.getCollectionById(collectionId);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecipesFromCollection(Long id, Integer limit, Integer page) throws NotFoundException {
        return service.getCollectionRecipesById(id, limit, page);
    }

    @Override
    public ResponseEntity<List<CollectionDto>> getCollectionsByName(String name,
                                                                    Integer limit, Integer page) throws
            NotFoundException {
        return service.getCollectionsByName(name, limit, page);
    }
}
