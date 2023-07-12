package voicerecipeserver.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.exceptions.InvalidMediaTypeException;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Valid
@RequestMapping(Constants.BASE_API_PATH + "/media")
public interface MediaApi {
    @GetMapping(value = "/{id}")
    ResponseEntity<byte[]> mediaGet(@PathVariable("id") @PositiveOrZero Long id) throws NotFoundException;

    @PostMapping
    ResponseEntity<IdDto> mediaPost(@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentTypeHeader,
                                    @RequestBody @NotNull byte[] data) throws InvalidMediaTypeException;

}
