package voicerecipeserver.security.service;

import io.jsonwebtoken.Claims;
import voicerecipeserver.security.domain.JwtAuthentication;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setLogin(claims.get("login", String.class));
        return jwtInfoToken;
    }

    private static Set<Role1> getRoles(Claims claims) {
        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(Role1::valueOf)
                .collect(Collectors.toSet());
    }
}