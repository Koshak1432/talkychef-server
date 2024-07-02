package talkychefserver.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import talkychefserver.config.Constants;
import talkychefserver.model.dto.CollectionDto;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.RecipeDto;

import java.util.List;

@Valid
@RequestMapping(Constants.BASE_API_PATH + "/collections")
public interface CollectionApi {

    @GetMapping
    ResponseEntity<List<CollectionDto>> getUserCollections(
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "limit", required = false) @PositiveOrZero Integer limit,
            @RequestParam(value = "page", required = false) @PositiveOrZero Integer page);

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PostMapping
    ResponseEntity<IdDto> addCollection(@Valid @RequestBody CollectionDto body);

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @DeleteMapping
    ResponseEntity<Void> deleteCollection(@RequestParam @PositiveOrZero Long id);

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PutMapping
    ResponseEntity<IdDto> updateCollection(@RequestParam("collection_id") @PositiveOrZero Long id,
                                           @Valid @RequestBody CollectionDto body);

    @PostMapping(value = "/liked")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<IdDto> addRecipeToLiked(@RequestParam("recipe_id") @PositiveOrZero Long id);

    @PostMapping(value = "/content")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<Void> addRecipeToCollection(@RequestParam("recipe_id") @PositiveOrZero Long recipeId,
                                               @RequestParam("collection_id") @NotBlank Long collectionId);


    @DeleteMapping(value = "/content")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<Void> deleteRecipeFromCollection(@RequestParam("recipe_id") @PositiveOrZero Long recipeId,
                                                    @RequestParam("collection_id") @NotBlank Long collectionId);

    @GetMapping(value = "/search")
    ResponseEntity<CollectionDto> getCollectionById(@RequestParam(value = "collection_id") Long collectionId);

    @GetMapping(value = "/{id}")
    ResponseEntity<List<RecipeDto>> getRecipesFromCollection(@PathVariable(value = "id") Long id,
                                                             @RequestParam(value = "limit", required = false) @PositiveOrZero Integer limit,
                                                             @RequestParam(value = "page", required = false) @PositiveOrZero Integer page);

    @GetMapping(value = "/search/{name}")
    ResponseEntity<List<CollectionDto>> getCollectionsByName(@PathVariable(value = "name") String name,
                                                             @RequestParam(value = "limit", required = false) @PositiveOrZero Integer limit,
                                                             @RequestParam(value = "page", required = false) @PositiveOrZero Integer page);
}
