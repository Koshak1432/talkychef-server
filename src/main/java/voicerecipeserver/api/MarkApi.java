package voicerecipeserver.api;

import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarkDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
@RequestMapping(Constants.BASE_API_PATH + "/marks")
@Validated
public interface MarkApi {

    @PostMapping
    ResponseEntity<IdDto> markPost(@RequestBody MarkDto mark) throws BadRequestException, NotFoundException, AuthException;

    @PutMapping
    ResponseEntity<IdDto> markUpdate(@RequestBody MarkDto mark) throws BadRequestException, NotFoundException;

    @DeleteMapping
    ResponseEntity<Void> markDelete(@RequestParam("user_uid") String userUid, @RequestParam("recipe_id") Long recipeId) throws NotFoundException;
}
