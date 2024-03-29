package talkychefserver.api;

import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import talkychefserver.config.Constants;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.MarkDto;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;

@RequestMapping(Constants.BASE_API_PATH + "/marks")
@Validated
public interface MarkApi {

    @GetMapping("/{id}")
    ResponseEntity<Float> getAvgMark(@PathVariable("id") @Positive Long id) throws NotFoundException;

    @GetMapping
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
