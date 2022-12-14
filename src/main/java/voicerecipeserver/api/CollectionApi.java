package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Valid
public interface CollectionApi {
    @PostMapping(value = "/api/v1/collection")
    ResponseEntity<Void> collectionPost(@RequestParam @NotBlank String name);

    @PostMapping(value = "/api/v1/collection/content")
    ResponseEntity<Void> collectionContentPost(@RequestParam @PositiveOrZero Long recipe, @RequestParam @NotBlank String collection) throws NotFoundException;

    @GetMapping(value = "/api/v1/collection/search", produces = "application/json")
    ResponseEntity<CollectionDto> collectionNameGet(@Size(max=128) @RequestParam("name") @NotBlank String name) throws NotFoundException;
}
