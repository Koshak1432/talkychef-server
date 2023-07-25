package voicerecipeserver.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarkDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.dto.UserProfileDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.model.exceptions.UserException;

import java.util.List;

@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
@RequestMapping(Constants.BASE_API_PATH + "/profile")
@Validated
public interface ProfileApi {

    @GetMapping
    ResponseEntity<UserProfileDto> profileGet() throws Exception;

    @PutMapping
    ResponseEntity<IdDto> profilePut(@RequestBody UserProfileDto profileDto) throws BadRequestException, NotFoundException;
   @PostMapping
    ResponseEntity<IdDto> profilePost(@RequestBody UserProfileDto profileDto) throws BadRequestException, NotFoundException, UserException;

    @GetMapping("/{login}")
    ResponseEntity<UserProfileDto> profileByIdGet(@PathVariable("login") @PositiveOrZero String login) throws Exception;


}
