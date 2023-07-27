package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.*;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.CollectionRepository;
import voicerecipeserver.respository.MarkRepository;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.security.service.impl.AuthServiceCommon;
import voicerecipeserver.services.CollectionService;

import java.util.List;
import java.util.Optional;

import static voicerecipeserver.security.service.impl.AuthServiceCommon.getAuthInfo;

@Service
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final MarkRepository markRepository;




    private final ModelMapper mapper;

    @Autowired
    public CollectionServiceImpl(CollectionRepository repository, RecipeRepository recipeRepository,
                                 UserRepository userRepository,
                                 MarkRepository markRepository, ModelMapper mapper) {
        this.collectionRepository = repository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.markRepository = markRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<Void> addCollection(String name) {
        Collection collection = new Collection();
        JwtAuthentication principal = getAuthInfo();
        if (principal == null) {
            return null;
        }
        collection.setAuthor(userRepository.findByUid(principal.getLogin()).orElseThrow(new ));
        collection.setName(name);
        collection.setNumber(0);
        collectionRepository.save(collection);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addRecipeToCollection(Long recipe, String collection) throws NotFoundException {
        Optional<Collection> collectionOptional = collectionRepository.findByName(collection);
        if (collectionOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти коллекцию с именем: " + collection);
        }

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipe);

        if (recipeOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти рецепт с id: " + recipe);
        }

        Collection collection1 = collectionOptional.get();
        Recipe recipe1 = recipeOptional.get();

        //после этой операции коллекция становится невалидной (да и в рецепте множество коллекций тоже)
        collectionRepository.addRecipeToCollection(recipe1.getId(), collection1.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CollectionDto> getCollectionPage(String name, Integer pageNum) throws NotFoundException, AuthException {
        Optional<Collection> collectionOptional = collectionRepository.findByName(name);

        if (null == pageNum) {
            pageNum = 0;
        }
        if (collectionOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти коллекцию с именем: " + name);
        }

        Collection collection = collectionOptional.get();

        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setName(name);
        collectionDto.setNumber(collection.getNumber());

        collectionDto.setRecipes(mapper.map(
                recipeRepository.findRecipesWithOffsetFromCollectionById(Constants.MAX_RECIPES_PER_PAGE,
                                                                         pageNum * Constants.MAX_RECIPES_PER_PAGE,
                                                                         collection.getId()),
                new TypeToken<List<RecipeDto>>() {}.getType()));
        for (RecipeDto recipeDto : collectionDto.getRecipes()) {
            setUserMark(recipeDto);
        }
        return ResponseEntity.ok(collectionDto);
    }

    private void setUserMark(RecipeDto recipe) throws AuthException {
        setUserMarkToRecipe(recipe, userRepository, markRepository);
    }

    static void setUserMarkToRecipe(RecipeDto recipe, UserRepository userRepository, MarkRepository markRepository) throws AuthException {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            JwtAuthentication principal = getAuthInfo();
            if (principal == null) {
                return;
            }
            User user = userRepository.findByUid(principal.getLogin()).orElseThrow(() -> new AuthException("Такой пользователь не зарегистрирован"));
            Optional<Mark> markOptional = markRepository.findById(new MarkKey(user.getId(), recipe.getId()));
            markOptional.ifPresent(mark -> recipe.setUserMark(mark.getMark()));
        }
    }
}
