package talkychefserver.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import talkychefserver.config.Constants;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.UserDto;
import talkychefserver.model.dto.UserProfileDto;

import java.util.List;

@RequestMapping(Constants.BASE_API_PATH + "/profile")
@Validated
public interface ProfileApi {

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<UserProfileDto> getCurrentUserProfile();

    @PutMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<IdDto> updateProfile(@RequestBody UserProfileDto profileDto);

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<IdDto> addProfile(@RequestBody UserProfileDto profileDto);

    @GetMapping("/search/{login}")
    ResponseEntity<List<UserProfileDto>> getProfilesByPartUid(
            @Size(max = 128) @NotBlank(message = "name must be not blank") @PathVariable("login") String login,
            @RequestParam(value = "limit", required = false) @Positive(message = "limit must be positive") Integer limit,
            @RequestParam(value = "page", required = false) @PositiveOrZero Integer page);

    @GetMapping("/{login}")
    ResponseEntity<UserProfileDto> getProfileByUid(
            @Size(max = 128) @NotBlank(message = "name must be not blank") @PathVariable("login") String login);

    @PostMapping("/restore-password")
    ResponseEntity<Void> sendInstructions(@RequestParam("email") String email);

    @GetMapping("/restore-password/{token}")
    ResponseEntity<IdDto> verifyCode(@PathVariable("token") String token);

    @PostMapping("/restore-password/{token}")
    ResponseEntity<Void> changePassword(@PathVariable("token") String token, @RequestBody UserDto userDto);


}
