package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.CollectionApi;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.CollectionService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@CrossOrigin(maxAge = 1440)
public class CollectionApiController implements CollectionApi {

    private final CollectionService service;

    @Autowired
    public CollectionApiController(CollectionService service){
        this.service = service;
    }


    @Override
    public ResponseEntity<Void> collectionPost(String name) {
        return service.addCollection(name);
    }

    @Override
    public ResponseEntity<Void> collectionContentPost(Long recipe, String collection) throws NotFoundException{
        return service.addRecipeToCollection(recipe,collection);
    }

    @Override
    public ResponseEntity<CollectionDto> collectionNameGet(String name, @Valid @PositiveOrZero Integer pageNum) throws NotFoundException {
        return service.getCollectionPage(name, pageNum);
    }
}
