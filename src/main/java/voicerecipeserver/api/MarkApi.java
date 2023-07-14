package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarkDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@RequestMapping(Constants.BASE_API_PATH + "/marks")
@Validated
public interface MarkApi {

    @PostMapping
    ResponseEntity<IdDto> markPost(@RequestBody MarkDto mark) throws BadRequestException, NotFoundException;

    @PutMapping
    ResponseEntity<IdDto> markUpdate(@RequestBody MarkDto mark) throws BadRequestException, NotFoundException;

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> markDelete(@PathVariable("id") Long id) throws NotFoundException;
}
