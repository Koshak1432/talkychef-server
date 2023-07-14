package voicerecipeserver.security.service;

import lombok.NonNull;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.security.dto.JwtRequest;
import voicerecipeserver.security.dto.JwtResponse;

public interface AuthService {
    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException;

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException;

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException;

    public JwtAuthentication getAuthInfo();

}