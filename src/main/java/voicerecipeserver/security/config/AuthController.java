package voicerecipeserver.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.security.dto.JwtRequest;
import voicerecipeserver.security.dto.JwtResponse;
import voicerecipeserver.security.dto.RefreshJwtRequest;
import voicerecipeserver.security.service.AuthService;
import voicerecipeserver.security.service.impl.AuthServiceImplMobile;
import voicerecipeserver.security.service.impl.AuthServiceImplWeb;

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