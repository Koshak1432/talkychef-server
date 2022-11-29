package voicerecipeserver.controllers;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import voicerecipeserver.api.IngredientApi;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.IngredientDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.Ingredient;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.model.mappers.DefaultMapper;
import voicerecipeserver.respository.IngredientRepository;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//TODO нет никаких проверок на существование :) .

@RestController
@CrossOrigin(maxAge = 1440)
public class IngredientApiController implements IngredientApi {

    private final IngredientRepository ingredientRepository;
    private final ModelMapper mapper;

    @Autowired
    public IngredientApiController(IngredientRepository ingredientRepository, ModelMapper mapper){
        this.ingredientRepository = ingredientRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<IngredientDto> ingredientIdGet(@PositiveOrZero Long id) throws NotFoundException {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);

        if(ingredientOptional.isEmpty()){
            throw new NotFoundException("ingredient with id " + id);
        }

        Ingredient ingredient = ingredientOptional.get();
        IngredientDto ingredientDto = new IngredientDto().id(ingredient.getId()).name(ingredient.getName());

        return new ResponseEntity<IngredientDto>(ingredientDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<IngredientDto>> ingredientSearchNameGet(String name){
        List<Ingredient> ingredients = ingredientRepository.findFirst5ByNameContaining(name.toLowerCase());

        List<IngredientDto> ingredientDtos = mapper.map(ingredients, new TypeToken<List<IngredientDto>>() {}.getType());

        return new ResponseEntity<List<IngredientDto>>(ingredientDtos, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<IdDto> ingredientPost(IngredientDto body){
        Ingredient ingredient = mapper.map(body, Ingredient.class);
        ingredient.setName(ingredient.getName().toLowerCase());

        ingredientRepository.save(ingredient);

        return new ResponseEntity<IdDto>(new IdDto().id(ingredient.getId()),HttpStatus.OK);
    }

}
