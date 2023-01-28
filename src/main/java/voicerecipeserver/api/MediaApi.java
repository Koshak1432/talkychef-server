package voicerecipeserver.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.exceptions.InvalidMediaTypeException;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Valid
@RequestMapping(Constants.BASE_API_PATH)
public interface MediaApi {
    @GetMapping(value = "/media/{id}")
    ResponseEntity<byte[]> mediaGet(@PathVariable("id") @PositiveOrZero Long id) throws NotFoundException;

    @PostMapping(value = "/media", produces = "application/json")
    ResponseEntity<IdDto> mediaPost(@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentTypeHeader, @RequestBody @NotNull byte[] data) throws InvalidMediaTypeException;

}
