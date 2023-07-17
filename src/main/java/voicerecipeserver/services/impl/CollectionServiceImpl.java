package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.Collection;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.CollectionRepository;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.security.service.impl.AuthServiceImpl;
import voicerecipeserver.services.CollectionService;

import java.util.List;
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
    @Transactional
    public ResponseEntity<Void> addRecipeToCollection(Long recipe, String collection) throws NotFoundException {
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

        //после этой операции коллекция становится невалидной (да и в рецепте множество коллекций тоже)
        collectionRepository.addRecipeToCollection(recipe1.getId(), collection1.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CollectionDto> getCollectionPage(String name, Integer pageNum) throws NotFoundException {
        Optional<Collection> collectionOptional = collectionRepository.findByName(name);

        if(null == pageNum){
            pageNum = 0;
        }
        if(collectionOptional.isEmpty()){
            throw new NotFoundException("Не удалось найти коллекцию с именем: " + name);
        }

        Collection collection = collectionOptional.get();

        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setName(name);
        collectionDto.setNumber(collection.getNumber());

        collectionDto.setRecipes(mapper.map(
                recipeRepository.findRecipesWithOffsetFromCollectionById(Constants.MAX_RECIPES_PER_PAGE, pageNum * Constants.MAX_RECIPES_PER_PAGE, collection.getId()),
                new TypeToken<List<RecipeDto>>() {}.getType()
        ));
        return new ResponseEntity<>(collectionDto, HttpStatus.OK);
    }
}
