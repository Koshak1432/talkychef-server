package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.IngredientApi;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.IngredientDto;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.IngredientService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

//TODO нет никаких проверок на существование :) .

@RestController
@CrossOrigin(maxAge = 1440)
public class IngredientApiController implements IngredientApi {

    private final IngredientService service;
    @Autowired
    public IngredientApiController(IngredientService service){
        this.service = service;
    }

    @Override
    public ResponseEntity<IngredientDto> ingredientIdGet(Long id) throws NotFoundException {
        return service.getIngredientById(id);
    }

    @Override
    public ResponseEntity<List<IngredientDto>> ingredientSearchNameGet(String name){
        return service.searchIngredientsByName(name);
    }

    @Override
    public ResponseEntity<IdDto> ingredientPost(IngredientDto body){
        return service.addIngredient(body);
    }

}
