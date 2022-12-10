package voicerecipeserver.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.CollectionApi;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.Collection;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.CollectionRepository;
import voicerecipeserver.respository.RecipeRepository;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 1440)
public class CollectionApiController implements CollectionApi {

    private final CollectionRepository collectionRepository;
    private final RecipeRepository recipeRepository;

    private final ModelMapper mapper;

    @Autowired
    public CollectionApiController(CollectionRepository repository, RecipeRepository recipeRepository, ModelMapper mapper){
        this.collectionRepository = repository;
        this.recipeRepository = recipeRepository;
        this.mapper = mapper;
    }


    @Override
    public ResponseEntity<Void> collectionPost(String name) {
        Collection collection = new Collection();
        collection.setName(name);
        collectionRepository.save(collection);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> collectionContentPost(Long recipe, String collection) throws NotFoundException{
        //TODO выглядит как-то слишком дорого, лучше написать свой запрос. Но перед этим проверить, мб все-таки не полностью сет грузит.
        Optional<Collection> collectionOptional = collectionRepository.findByName(collection);
        if(collectionOptional.isEmpty()){
            System.out.println("TESTTTTTTTTTTTTTT1");
            throw new NotFoundException("Can't find collection with name: " + collection);
        }

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipe);

        if(recipeOptional.isEmpty()){
            System.out.println("TESTTTTTTTTTTTTTT2");
            throw new NotFoundException("Can't find recipe with id: " + recipe);
        }

        Collection collection1 = collectionOptional.get();
        Recipe recipe1 = recipeOptional.get();
    System.out.println("TESTTTTTTTTTTTTTT3");
        collection1.addRecipe(recipe1);

        collectionRepository.save(collection1);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CollectionDto> collectionNameGet(String name) throws NotFoundException {
        Optional<Collection> collectionOptional = collectionRepository.findByName(name);
        if(collectionOptional.isEmpty()){
            throw new NotFoundException("Can't find collection with name: " + name);
        }

        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setName(name);

        Collection collection = collectionOptional.get();

        for (Recipe recipe : collection.getRecipes()){
            collectionDto.addRecipesItem(mapper.map(recipe, RecipeDto.class));
        }
        return new ResponseEntity<>(collectionDto, HttpStatus.OK);
    }
}
