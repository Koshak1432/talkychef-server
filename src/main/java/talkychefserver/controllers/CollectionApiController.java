package talkychefserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import talkychefserver.api.CollectionApi;
import talkychefserver.model.dto.CollectionDto;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.RecipeDto;
import talkychefserver.services.interfaces.CollectionService;

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
    public ResponseEntity<List<CollectionDto>> getUserCollections(String login, Integer limit, Integer page) {
        return service.getCollections(login, limit, page);
    }

    @Override
    public ResponseEntity<IdDto> addCollection(CollectionDto body) {
        return service.addCollection(body);
    }

    @Override
    public ResponseEntity<Void> deleteCollection(Long id) {
        return service.deleteCollection(id);

    }

    @Override
    public ResponseEntity<IdDto> updateCollection(Long id, CollectionDto body) {
        return service.putCollection(id, body);

    }

    @Override
    public ResponseEntity<IdDto> addRecipeToLiked(Long recipeId) {
        return service.postLikedRecipe(recipeId);
    }

    @Override
    public ResponseEntity<Void> deleteRecipeFromCollection(Long recipe, Long collectionId) {
        return service.deleteRecipeFromCollection(recipe, collectionId);
    }

    @Override
    public ResponseEntity<Void> addRecipeToCollection(Long recipe, Long collectionId) {
        return service.addRecipeToCollection(recipe, collectionId);
    }

    @Override
    public ResponseEntity<CollectionDto> getCollectionById(Long collectionId) {
        return service.getCollectionById(collectionId);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecipesFromCollection(Long id, Integer limit, Integer page) {
        return service.getCollectionRecipesById(id, limit, page);
    }

    @Override
    public ResponseEntity<List<CollectionDto>> getCollectionsByName(String name, Integer limit, Integer page) {
        return service.getCollectionsByName(name, limit, page);
    }
}
