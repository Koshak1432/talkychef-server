package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import voicerecipeserver.api.IngredientApi;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.IngredientDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.model.entities.Ingredient;
import voicerecipeserver.model.mappers.DefaultMapper;
import voicerecipeserver.respository.IngredientRepository;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

//TODO нет никаких проверок на существование :) .

@RestController
public class IngredientApiController implements IngredientApi {

    private final IngredientRepository ingredientRepository;
    private final DefaultMapper mapper;

    @Autowired
    public IngredientApiController(IngredientRepository ingredientRepository, DefaultMapper mapper){
        this.ingredientRepository = ingredientRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<IngredientDto> ingredientIdGet(@PositiveOrZero Long id){
        Ingredient ingredient = ingredientRepository.findById(id).get();

        IngredientDto ingredientDto = new IngredientDto().id(ingredient.getId()).name(ingredient.getName()).measureUnitSetId(ingredient.getSet().getId());

        return new ResponseEntity<IngredientDto>(ingredientDto, HttpStatus.OK);
    }

    //TODO поиск нужно делать, а не соло поиск, проверочку на существование.
    @Override
    public ResponseEntity<List<IngredientDto>> ingredientSearchNameGet(String name){
        Ingredient ingredient = ingredientRepository.findByName(name).get();

        IngredientDto ingredientDto = new IngredientDto().id(ingredient.getId()).name(ingredient.getName()).measureUnitSetId(ingredient.getSet().getId());

        ArrayList<IngredientDto> res = new ArrayList<IngredientDto>();
        res.add(ingredientDto);

        return new ResponseEntity<List<IngredientDto>>(res, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<IdDto> ingredientPost(IngredientDto body){
        Ingredient ingredient = mapper.map(body, Ingredient.class);

        ingredientRepository.save(ingredient);

        return new ResponseEntity<IdDto>(new IdDto().id(ingredient.getId()),HttpStatus.OK);
    }

}
