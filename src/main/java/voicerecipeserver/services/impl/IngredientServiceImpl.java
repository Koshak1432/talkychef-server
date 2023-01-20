package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.IngredientDto;
import voicerecipeserver.model.entities.Ingredient;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.IngredientRepository;
import voicerecipeserver.services.IngredientService;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final ModelMapper mapper;

    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository, ModelMapper mapper){
        this.ingredientRepository = ingredientRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<IngredientDto> getIngredientById(Long id) throws NotFoundException {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);

        if(ingredientOptional.isEmpty()){
            throw new NotFoundException("ingredient with id " + id);
        }

        Ingredient ingredient = ingredientOptional.get();
        IngredientDto ingredientDto = new IngredientDto().id(ingredient.getId()).name(ingredient.getName());

        return new ResponseEntity<IngredientDto>(ingredientDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<IngredientDto>> searchIngredientsByName(String name) {
        List<Ingredient> ingredients = ingredientRepository.findFirst5ByNameContaining(name.toLowerCase());

        List<IngredientDto> ingredientDtos = mapper.map(ingredients, new TypeToken<List<IngredientDto>>() {}.getType());

        return new ResponseEntity<List<IngredientDto>>(ingredientDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> addIngredient(IngredientDto body) {
        Ingredient ingredient = mapper.map(body, Ingredient.class);
        ingredient.setName(ingredient.getName().toLowerCase());

        ingredientRepository.save(ingredient);

        return new ResponseEntity<IdDto>(new IdDto().id(ingredient.getId()),HttpStatus.OK);
    }
}
