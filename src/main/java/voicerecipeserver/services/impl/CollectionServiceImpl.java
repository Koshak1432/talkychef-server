package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.entities.Collection;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.CollectionRepository;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.services.CollectionService;
import voicerecipeserver.utils.FindUtils;

import java.util.List;

import static voicerecipeserver.security.service.impl.AuthServiceCommon.getAuthInfo;
import static voicerecipeserver.security.service.impl.AuthServiceCommon.getUserLogin;
import static voicerecipeserver.utils.FindUtils.*;

@Service
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;


    private final ModelMapper mapper;

    @Autowired
    public CollectionServiceImpl(CollectionRepository repository, RecipeRepository recipeRepository,
                                 UserRepository userRepository, ModelMapper mapper) {
        this.collectionRepository = repository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<IdDto> addCollection(String name) throws NotFoundException {
        Collection collection = new Collection();
        JwtAuthentication principal = getAuthInfo();
        if (principal == null) {
            return null;
        }
        collection.setAuthor(findUser(userRepository, principal.getLogin()));
        collection.setName(name);
        collection.setNumber(0);
        collectionRepository.save(collection);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addRecipeToCollection(Long recipeId, Long collectionId) throws NotFoundException,
            AuthException {
        User user = findUser(userRepository, getUserLogin());
        Collection collection = FindUtils.findCollection(collectionRepository, collectionId);
        if (!user.equals(collection.getAuthor())) {
            throw new AuthException("No rights");
        }
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        collectionRepository.addRecipeToCollection(recipe.getId(), collection.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CollectionDto> getCollectionPage(Long collectionId) throws NotFoundException {
        Collection collection = FindUtils.findCollection(collectionRepository, collectionId);
        CollectionDto collectionDto = mapper.map(collection, CollectionDto.class);
        return ResponseEntity.ok(collectionDto);
    }

    @Override
    public ResponseEntity<Void> deleteCollection(Long id) throws NotFoundException, AuthException {
        User user = findUser(userRepository, getUserLogin());
        Collection collection = findCollection(collectionRepository, id);
        if (collection.getAuthor() == null || !collection.getAuthor().equals(user)) {
            throw new AuthException("No rights");
        }
        collectionRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> putCollection(Long id, String name) throws AuthException, NotFoundException {
        User user = findUser(userRepository, getUserLogin());
        Collection collection = findCollection(collectionRepository, id);
        if (collection.getAuthor() == null || !collection.getAuthor().equals(user)) {
            throw new AuthException("No rights");
        }
        collection.setName(name);
        Collection savedCollection = collectionRepository.save(collection);
        return ResponseEntity.ok(new IdDto().id(savedCollection.getId()));
    }

    @Override
    public ResponseEntity<Void> deleteRecipeFromCollection(Long recipeId, Long collectionId) throws NotFoundException,
            AuthException {
        User user = findUser(userRepository, getUserLogin());
        Collection collection = FindUtils.findCollection(collectionRepository, collectionId);
        if (collection.getAuthor() == null || !user.equals(collection.getAuthor())) {
            throw new AuthException("No rights");
        }
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        collectionRepository.deleteRecipeFromCollection(recipe.getId(), collection.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CollectionDto>> getCollection(String login) throws NotFoundException {
        if (login == null) {
            login = getUserLogin();
        }
        User user = findUser(userRepository, login);
        List<Collection> collections = collectionRepository.findByAuthorId(user.getId());
        List<CollectionDto> collectionDtos = collections.stream().map(
                collection -> mapper.map(collection, CollectionDto.class)).toList();
        return ResponseEntity.ok(collectionDtos);
    }

    @Override
    public ResponseEntity<List<CollectionDto>> getCollectionPageByName(String name, Long limit) throws
            NotFoundException {
        if (null == limit) {
            limit = 0L;
        }
        List<Collection> collections = findCollectionsByName(name, limit);
        List<CollectionDto> collectionDtos = collections.stream().map(
                collection -> mapper.map(collection, CollectionDto.class)).toList();
        return ResponseEntity.ok(collectionDtos);
    }

    private List<Collection> findCollectionsByName(String name, Long limit) throws NotFoundException {
        List<Collection> collections = collectionRepository.findByNameContaining(limit, name);
        if (collections.isEmpty()) {
            throw new NotFoundException("Couldn't find collections with substring: " + name);
        }
        return collections;
    }
}
