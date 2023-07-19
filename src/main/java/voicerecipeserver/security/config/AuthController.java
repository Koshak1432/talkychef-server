package voicerecipeserver.security.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;

import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.security.dto.JwtRequest;
import voicerecipeserver.security.dto.JwtResponse;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.security.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(Constants.BASE_API_PATH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/registration")
    public ResponseEntity<JwtResponse> registration(@RequestBody UserDto user) throws AuthException, NotFoundException, BadRequestException {
        final JwtResponse token = authService.registration(user);
        return ResponseEntity.ok(token);
    }




    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@CookieValue(value = "refreshToken", required = true) String refreshToken) throws AuthException {
        final JwtResponse token = authService.getAccessToken(refreshToken);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@CookieValue(value = "refreshToken", required = true) String refreshToken) throws AuthException {
        final JwtResponse token = authService.refresh(refreshToken);
        return ResponseEntity.ok(token);
    }

    @PutMapping("/user/password")
    public ResponseEntity<JwtResponse> userUpdate(@RequestBody UserDto userDto) throws NotFoundException, BadRequestException, AuthException {
        final JwtResponse token = authService.changePassword(userDto);
        return ResponseEntity.ok(token);
    }


}