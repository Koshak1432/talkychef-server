package talkychefserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkychefserver.model.dto.CollectionDto;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.RecipeDto;
import talkychefserver.model.entities.Collection;
import talkychefserver.model.entities.Media;
import talkychefserver.model.entities.Recipe;
import talkychefserver.model.entities.User;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.respositories.*;
import talkychefserver.security.service.impl.AuthServiceCommon;
import talkychefserver.services.interfaces.CollectionService;
import talkychefserver.utils.FindUtils;
import talkychefserver.utils.GetUtil;

import java.util.List;


@Service
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;


    private final ModelMapper mapper;
    private final CategoryRepository categoryRepository;


    @Autowired
    public CollectionServiceImpl(CollectionRepository repository, RecipeRepository recipeRepository,
                                 UserRepository userRepository, MediaRepository mediaRepository, ModelMapper mapper,
                                 CategoryRepository categoryRepository) {
        this.collectionRepository = repository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> addCollection(CollectionDto body) {
        FindUtils.findMedia(mediaRepository, body.getMediaId());
        Collection collection = mapper.map(body, Collection.class);
        collection.setAuthor(FindUtils.findUserByUid(userRepository, AuthServiceCommon.getUserLogin()));
        collection.setNumber(0);
        collectionRepository.save(collection);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addRecipeToCollection(Long recipeId, Long collectionId) {
        User user = FindUtils.findUserByUid(userRepository, AuthServiceCommon.getUserLogin());
        Collection collection = FindUtils.findCollection(collectionRepository, collectionId);
        if (!user.equals(collection.getAuthor())) {
            throw new AuthException("No rights");
        }
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        collectionRepository.addRecipeToCollection(recipe.getId(), collection.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CollectionDto> getCollectionById(Long collectionId) {
        Collection collection = FindUtils.findCollection(collectionRepository, collectionId);
        CollectionDto collectionDto = mapper.map(collection, CollectionDto.class);
        return ResponseEntity.ok(collectionDto);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteCollection(Long id) {
        User user = FindUtils.findUserByUid(userRepository, AuthServiceCommon.getUserLogin());
        Collection collection = FindUtils.findCollection(collectionRepository, id);
        if (collection.getAuthor() == null || !collection.getAuthor().getUid().equals(user.getUid())) {
            throw new AuthException("No rights");
        }
        collectionRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> putCollection(Long id, CollectionDto body) {
        User user = FindUtils.findUserByUid(userRepository, AuthServiceCommon.getUserLogin());
        Collection collection = FindUtils.findCollection(collectionRepository, id);
        if (collection.getAuthor() == null || !collection.getAuthor().getUid().equals(user.getUid())) {
            throw new AuthException("No rights");
        }
        collection.setName(body.getName());
        if (body.getMediaId() != null) {
            Media media = FindUtils.findMedia(mediaRepository, body.getMediaId());
            collection.setMedia(media);
        }
        Collection savedCollection = collectionRepository.save(collection);
        return ResponseEntity.ok(new IdDto().id(savedCollection.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteRecipeFromCollection(Long recipeId, Long collectionId) {
        User user = FindUtils.findUserByUid(userRepository, AuthServiceCommon.getUserLogin());
        Collection collection = FindUtils.findCollection(collectionRepository, collectionId);
        if (collection.getAuthor() == null || !user.equals(collection.getAuthor())) {
            throw new AuthException("No rights");
        }
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        collectionRepository.deleteRecipeFromCollection(recipe.getId(), collection.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CollectionDto>> getCollections(String login, Integer limit, Integer page) {
        String findLogin = login == null ? AuthServiceCommon.getUserLogin() : login;
        User user = FindUtils.findUserByUid(userRepository, findLogin);
        List<Collection> collections = collectionRepository.findByAuthorIdWithOffset(user.getId(),
                                                                                     GetUtil.getCurrentLimit(limit),
                                                                                     GetUtil.getCurrentPage(page));
        List<CollectionDto> collectionDtos = collections.stream().map(
                collection -> mapper.map(collection, CollectionDto.class)).toList();
        return ResponseEntity.ok(collectionDtos);
    }

    @Override
    public ResponseEntity<List<CollectionDto>> getCollectionsByName(String name, Integer limit, Integer page) {
        List<Collection> collections = collectionRepository.findByNameContaining(name, GetUtil.getCurrentLimit(limit),
                                                                                 GetUtil.getCurrentPage(page));
        List<CollectionDto> collectionDtos = collections.stream().map(
                collection -> mapper.map(collection, CollectionDto.class)).toList();
        return ResponseEntity.ok(collectionDtos);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getCollectionRecipesById(Long id, Integer limit, Integer page) {
        FindUtils.findCollectionById(collectionRepository, id);
        List<Recipe> recipes = recipeRepository.findByCollectionId(id, GetUtil.getCurrentLimit(limit),
                                                                   GetUtil.getCurrentPage(page));
        List<RecipeDto> recipeDtos = recipes.stream().map(element -> mapper.map(element, RecipeDto.class)).toList();
        return ResponseEntity.ok(recipeDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> postLikedRecipe(Long recipeId) {
        FindUtils.findRecipe(recipeRepository, recipeId);
        String login = AuthServiceCommon.getUserLogin();
        String likedName = login + "_liked";
        Collection likedCollection = collectionRepository.findCollectionByName(likedName).orElse(null);
        if (likedCollection == null) {
            likedCollection = collectionRepository.save(
                    new Collection(likedName, 0, FindUtils.findUserByUid(userRepository, login)));
        }
        if (collectionRepository.findRecipeInCollection(recipeId, likedCollection.getId()).isEmpty()) {
            collectionRepository.addRecipeToCollection(recipeId, likedCollection.getId());
        }
        return ResponseEntity.ok(new IdDto().id(likedCollection.getId()));
    }
}
