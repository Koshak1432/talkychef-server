package voicerecipeserver.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.security.service.impl.JwtProviderImpl;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public interface JwtProvider  {

    public String generateAccessToken(@NonNull User user) ;
    public String generateRefreshToken(@NonNull User user) ;

    public boolean validateAccessToken(@NonNull String accessToken);

    public boolean validateRefreshToken(@NonNull String refreshToken) ;

    public Claims getAccessClaims(@NonNull String token);

    public Claims getRefreshClaims(@NonNull String token);

}