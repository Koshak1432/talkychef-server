package voicerecipeserver.security.service.impl;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.security.dto.JwtRequest;
import voicerecipeserver.security.dto.JwtResponse;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.AuthException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import voicerecipeserver.security.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userServiceImpl;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProviderImpl jwtProviderImpl;

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final User user = userServiceImpl.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProviderImpl.generateAccessToken(user);
            final String refreshToken = jwtProviderImpl.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.accessToken(accessToken).refreshToken(refreshToken);
            return jwtResponse;
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProviderImpl.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userServiceImpl.getByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProviderImpl.generateAccessToken(user);
                JwtResponse jwtResponse = new JwtResponse();
                jwtResponse.accessToken(accessToken);
                return jwtResponse;
            }
        }
        return new JwtResponse();
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProviderImpl.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userServiceImpl.getByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProviderImpl.generateAccessToken(user);
                final String newRefreshToken = jwtProviderImpl.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                JwtResponse jwtResponse = new JwtResponse();
                jwtResponse.accessToken(accessToken).refreshToken(newRefreshToken);
                return jwtResponse;
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}