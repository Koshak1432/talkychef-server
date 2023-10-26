package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.CollectionApi;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.CollectionService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;

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
    public ResponseEntity<List<CollectionDto>> collectionGet(String login) throws NotFoundException {
        return service.getCollections(login);
    }

    @Override
    public ResponseEntity<IdDto> collectionPost(CollectionDto body) throws NotFoundException {
        return service.addCollection(body);
    }

    @Override
    public ResponseEntity<Void> collectionDelete(Long id) throws NotFoundException, AuthException {
        return service.deleteCollection(id);

    }

    @Override
    public ResponseEntity<IdDto> collectionPut(Long id, CollectionDto body) throws NotFoundException, AuthException {
        return service.putCollection(id, body);

    }

    @Override
    public ResponseEntity<IdDto> collectionLikedRecipePost(Long recipeId) throws NotFoundException {
        return service.postLikedRecipe(recipeId);
    }

    @Override
    public ResponseEntity<Void> collectionContentDelete(Long recipe, Long collectionId) throws NotFoundException, AuthException {
        return service.deleteRecipeFromCollection(recipe, collectionId);
    }

    @Override
    public ResponseEntity<Void> collectionContentPost(Long recipe, Long collectionId) throws NotFoundException, AuthException {
        return service.addRecipeToCollection(recipe, collectionId);
    }

    @Override
    public ResponseEntity<CollectionDto> collectionGetByName(Long collectionId) throws NotFoundException, AuthException, BadRequestException {
            return service.getCollectionPage(collectionId);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecipesFromCollection(Long id) throws NotFoundException, AuthException, BadRequestException {
        return service.getCollectionRecipesById(id);
    }

    @Override
    public ResponseEntity<List<CollectionDto>> collectionGetByName(String name, @Valid @PositiveOrZero Long limit) throws NotFoundException, AuthException, BadRequestException {
        return service.getCollectionPageByName(name, limit);
    }
}
