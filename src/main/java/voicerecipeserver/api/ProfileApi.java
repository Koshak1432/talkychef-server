package voicerecipeserver.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.*;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.model.exceptions.UserException;

import java.util.List;
import java.util.UUID;

@RequestMapping(Constants.BASE_API_PATH + "/profile")
@Validated
public interface ProfileApi {

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")

    ResponseEntity<UserProfileDto> profileGet() throws Exception;

    @PutMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")

    ResponseEntity<IdDto> profilePut(@RequestBody UserProfileDto profileDto) throws BadRequestException, NotFoundException;

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")

    ResponseEntity<IdDto> profilePost(@RequestBody UserProfileDto profileDto) throws BadRequestException, NotFoundException, UserException;

    @GetMapping("/{login}")
    ResponseEntity<List<UserProfileDto>> profileByUidGet(
            @Size(max = 128) @NotBlank(message = "name must be not blank") @PathVariable("login") String login,
            @RequestParam(value = "limit", required = false) @Positive(message = "limit must be positive") Integer limit) throws Exception;

    @PostMapping("/restore-password")
    public ResponseEntity<Void> sendInstructions(@RequestParam("email") String email) throws NotFoundException;

    @GetMapping("/restore-password/{token}")
    public ResponseEntity<IdDto> verifyCode(@PathVariable("token") String token) throws NotFoundException, BadRequestException;

    @PostMapping("/restore-password/{token}")
    public ResponseEntity<Void> changePassword(@PathVariable("token") String token, @RequestBody UserDto userDto) throws NotFoundException, BadRequestException, AuthException;



}
