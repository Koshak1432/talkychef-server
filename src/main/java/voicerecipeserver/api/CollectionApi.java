package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

@Valid
@RequestMapping(Constants.BASE_API_PATH + "/collections")
public interface CollectionApi {

    @GetMapping
    ResponseEntity<List<CollectionDto>> collectionGet(@RequestParam(value = "name", required = false) String login) throws NotFoundException;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PostMapping
    ResponseEntity<IdDto> collectionPost(@RequestParam("name") @NotBlank String name) throws NotFoundException;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @DeleteMapping
    ResponseEntity<Void> collectionDelete(@RequestParam @PositiveOrZero Long id) throws NotFoundException, AuthException;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PutMapping
    ResponseEntity<IdDto> collectionPut(@RequestParam("collection_id") @PositiveOrZero Long id, @RequestParam("name") @NotBlank String name) throws NotFoundException, AuthException;


    @PostMapping(value = "/content")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<Void> collectionContentPost(@RequestParam("recipe_id") @PositiveOrZero Long recipeId,
                                               @RequestParam("collection_id") @NotBlank Long collectionId) throws NotFoundException, AuthException;

    @DeleteMapping(value = "/content")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<Void> collectionContentDelete(@RequestParam("recipe_id") @PositiveOrZero Long recipeId,
                                                 @RequestParam("collection_id") @NotBlank Long collectionId) throws NotFoundException, AuthException;

    @GetMapping(value = "/search")
    ResponseEntity<CollectionDto> collectionIdGet(@RequestParam(value = "collection_id") Long collectionId)
            throws NotFoundException, AuthException, BadRequestException;
    @GetMapping(value = "/search/{name}")
    ResponseEntity<List<CollectionDto>> collectionIdGet(@PathVariable(value = "name") String name,
                                                  @RequestParam(value = "limit", required = false) @PositiveOrZero Long limit)
            throws NotFoundException, AuthException, BadRequestException;
}
