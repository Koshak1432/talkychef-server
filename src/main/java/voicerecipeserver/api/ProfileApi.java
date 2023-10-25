package voicerecipeserver.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.dto.UserProfileDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

@RequestMapping(Constants.BASE_API_PATH + "/profile")
@Validated
public interface ProfileApi {

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<UserProfileDto> profileGet() throws NotFoundException;

    @PutMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<IdDto> profilePut(@RequestBody UserProfileDto profileDto) throws BadRequestException,
            NotFoundException;

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<IdDto> profilePost(@RequestBody UserProfileDto profileDto) throws BadRequestException,
            NotFoundException;

    @GetMapping("/search/{login}")
    ResponseEntity<List<UserProfileDto>> profilesByPartUidGet(
            @Size(max = 128) @NotBlank(message = "name must be not blank") @PathVariable("login") String login,
            @RequestParam(value = "limit", required = false) @Positive(message = "limit must be positive") Integer limit,
            @RequestParam(value = "page", required = false) @PositiveOrZero Integer page) throws NotFoundException;

    @GetMapping("/{login}")
    ResponseEntity<UserProfileDto> profileByUidGet(
            @Size(max = 128) @NotBlank(message = "name must be not blank") @PathVariable("login") String login) throws
            NotFoundException;

    @PostMapping("/restore-password")
    ResponseEntity<Void> sendInstructions(@RequestParam("email") String email) throws NotFoundException;

    @GetMapping("/restore-password/{token}")
    ResponseEntity<IdDto> verifyCode(@PathVariable("token") String token) throws NotFoundException,
            BadRequestException;

    @PostMapping("/restore-password/{token}")
    ResponseEntity<Void> changePassword(@PathVariable("token") String token, @RequestBody UserDto userDto) throws
            NotFoundException, BadRequestException, AuthException;


}
