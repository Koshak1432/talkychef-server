package voicerecipeserver.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import voicerecipeserver.config.Constants;
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.security.service.impl.AuthServiceImpl;

@RestController
//@RequestMapping(Constants.BASE_API_PATH)
@RequiredArgsConstructor
public class Controller {

    private final AuthServiceImpl authService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello user  + !");
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("hello/user")
    public ResponseEntity<String> helloUser() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello user " + authInfo.getPrincipal() + "!");
    }

    @GetMapping("hello/user1")
    public ResponseEntity<String> helloUser1() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello! user " + authInfo.getPrincipal() + "!");
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("hello/admin")
    public ResponseEntity<String> helloAdmin() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello admin " + authInfo.getPrincipal() + "!");
    }

}
