package talkychefserver.security.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import talkychefserver.config.Constants;
import talkychefserver.model.dto.UserDto;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;
import talkychefserver.security.dto.JwtRequest;
import talkychefserver.security.dto.JwtResponse;
import talkychefserver.security.dto.RefreshJwtRequest;
import talkychefserver.security.service.AuthService;

@RestController
@RequestMapping(Constants.BASE_API_PATH)
public class AuthController {

    private final AuthService authServiceMobile;
    private final AuthService authServiceWeb;

    public AuthController(@Qualifier("authServiceImplMobile") AuthService authServiceMobile,
                          @Qualifier("authServiceImplWeb") AuthService authServiceWeb) {
        this.authServiceMobile = authServiceMobile;
        this.authServiceWeb = authServiceWeb;
    }

    @PostMapping("/registration/mobile")
    public ResponseEntity<JwtResponse> registrationMobile(@RequestBody UserDto user) throws AuthException,
            NotFoundException, BadRequestException {
        final JwtResponse token = authServiceMobile.registration(user);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/registration/web")
    public ResponseEntity<JwtResponse> registrationWeb(@RequestBody UserDto user) throws AuthException,
            NotFoundException, BadRequestException {
        final JwtResponse token = authServiceWeb.registration(user);
        return ResponseEntity.ok(token);
    }


    @PostMapping("/login/mobile")
    public ResponseEntity<JwtResponse> loginMobile(@RequestBody JwtRequest authRequest) throws AuthException,
            NotFoundException {
        final JwtResponse token = authServiceMobile.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login/web")
    public ResponseEntity<JwtResponse> loginWeb(@RequestBody JwtRequest authRequest) throws AuthException,
            NotFoundException {
        final JwtResponse token = authServiceWeb.login(authRequest);
        return ResponseEntity.ok(token);
    }


    @PostMapping("/auth/refresh/web")
    public ResponseEntity<JwtResponse> getNewRefreshTokenWeb(
            @CookieValue(value = "refreshToken") String refreshToken) throws AuthException, NotFoundException {
        final JwtResponse token = authServiceWeb.refresh(refreshToken);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/refresh/mobile")
    public ResponseEntity<JwtResponse> getNewRefreshMobile(@RequestBody RefreshJwtRequest request) throws AuthException,
            NotFoundException {
        final JwtResponse token = authServiceMobile.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }


    @PostMapping("/auth/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException,
            NotFoundException {
        final JwtResponse token = authServiceMobile.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }


    @PutMapping("/profile/password/mobile")
    public ResponseEntity<JwtResponse> userUpdateMobile(@RequestBody UserDto userDto) throws NotFoundException,
            AuthException, BadRequestException {
        final JwtResponse token = authServiceMobile.changePassword(userDto);
        return ResponseEntity.ok(token);
    }

    @PutMapping("/profile/password/web")
    public ResponseEntity<JwtResponse> userUpdateWeb(@RequestBody UserDto userDto) throws NotFoundException,
            AuthException, BadRequestException {
        final JwtResponse token = authServiceWeb.changePassword(userDto);
        return ResponseEntity.ok(token);
    }


}