package voicerecipeserver.api;

import jakarta.validation.constraints.Positive;
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

@RequestMapping(Constants.BASE_API_PATH + "/marks")
@Validated
public interface MarkApi {

    @GetMapping("/{id}")
    ResponseEntity<Float> getAvgMark(@PathVariable("id") @Positive Long id) throws NotFoundException;

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<MarkDto> getMark(@RequestParam("user_uid") String userUid,
                                    @RequestParam("recipe_id") Long recipeId) throws NotFoundException;

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<IdDto> markPost(@RequestBody MarkDto mark) throws BadRequestException, NotFoundException,
            AuthException;

    @PutMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<IdDto> markUpdate(@RequestBody MarkDto mark) throws BadRequestException, NotFoundException,
            AuthException;

    @DeleteMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<Void> markDelete(@RequestParam("user_uid") String userUid,
                                    @RequestParam("recipe_id") Long recipeId) throws NotFoundException, AuthException,
            BadRequestException;
}
