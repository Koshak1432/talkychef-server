package voicerecipeserver.security.service;

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

import java.util.HashMap;
import java.util.Map;

public interface AuthService {
    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException;

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException;

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException;

    public JwtAuthentication getAuthInfo();

}