package talkychefserver.services.interfaces;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.CollectionDto;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.RecipeDto;

import java.util.List;

public interface CollectionService {
    ResponseEntity<IdDto> addCollection(CollectionDto body);

    ResponseEntity<Void> addRecipeToCollection(Long recipe, Long collectionId);

    ResponseEntity<CollectionDto> getCollectionById(Long collectionId);

    ResponseEntity<Void> deleteCollection(Long id);

    ResponseEntity<IdDto> putCollection(Long id, CollectionDto body);

    ResponseEntity<Void> deleteRecipeFromCollection(Long recipe, Long collectionId);

    ResponseEntity<List<CollectionDto>> getCollections(String login, Integer limit, Integer page);

    ResponseEntity<List<CollectionDto>> getCollectionsByName(String name, Integer limit, Integer page);

    ResponseEntity<List<RecipeDto>> getCollectionRecipesById(Long id, Integer limit, Integer page);

    ResponseEntity<IdDto> postLikedRecipe(Long recipeId);
}
