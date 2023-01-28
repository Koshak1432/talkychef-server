package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.Collection;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.CollectionRepository;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.services.CollectionService;

import java.util.Optional;

@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final RecipeRepository recipeRepository;

    private final ModelMapper mapper;

    @Autowired
    public CollectionServiceImpl(CollectionRepository repository, RecipeRepository recipeRepository, ModelMapper mapper){
        this.collectionRepository = repository;
        this.recipeRepository = recipeRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<Void> addCollection(String name) {
        Collection collection = new Collection();
        collection.setName(name);
        collection.setNumber(0);
        collectionRepository.save(collection);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> addRecipeToCollection(Long recipe, String collection) throws NotFoundException {
        //TODO выглядит как-то слишком дорого, лучше написать свой запрос. Но перед этим проверить, мб все-таки не полностью сет грузит.
        Optional<Collection> collectionOptional = collectionRepository.findByName(collection);
        if(collectionOptional.isEmpty()){
            throw new NotFoundException("Не удалось найти коллекцию с именем: " + collection);
        }

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipe);

        if(recipeOptional.isEmpty()){
            throw new NotFoundException("Не удалось найти рецепт с id: " + recipe);
        }

        Collection collection1 = collectionOptional.get();
        Recipe recipe1 = recipeOptional.get();
        collection1.addRecipe(recipe1);

        collectionRepository.save(collection1);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CollectionDto> getCollectionByName(String name) throws NotFoundException {
        Optional<Collection> collectionOptional = collectionRepository.findByName(name);
        if(collectionOptional.isEmpty()){
            throw new NotFoundException("Не удалось найти коллекцию с именем: " + name);
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
