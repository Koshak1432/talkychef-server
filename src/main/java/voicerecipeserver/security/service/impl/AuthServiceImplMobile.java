package voicerecipeserver.security.service.impl;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.security.config.BeanConfig;
import voicerecipeserver.security.dto.JwtRequest;
import voicerecipeserver.security.dto.JwtResponse;
import voicerecipeserver.security.service.AuthService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service

public class AuthServiceImplMobile implements AuthService {
    private final BeanConfig passwordEncoder;
    private final UserServiceImpl userServiceImpl;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProviderImpl jwtProviderImpl;

    @Autowired
    public AuthServiceImplMobile(BeanConfig passwordEncoder, UserServiceImpl userServiceImpl,
                                 JwtProviderImpl jwtProviderImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userServiceImpl = userServiceImpl;
        this.jwtProviderImpl = jwtProviderImpl;
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final User user = userServiceImpl.getByLogin(authRequest.getLogin()).orElseThrow(
                () -> new AuthException(Constants.USER_NOT_EXISTS_MSG));
        if (passwordEncoder.getPasswordEncoder().matches(authRequest.getPassword(), user.getPassword())) {
            return getJwtResponse(user);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    private JwtResponse getJwtResponse(User user) {
        final String accessToken = jwtProviderImpl.generateAccessToken(user);
        final String refreshToken = jwtProviderImpl.generateRefreshToken(user);
        refreshStorage.put(user.getUid(), refreshToken);
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.accessToken(accessToken).refreshToken(refreshToken);
        return jwtResponse;
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProviderImpl.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userServiceImpl.getByLogin(login).orElseThrow(
                        () -> new AuthException(Constants.USER_NOT_EXISTS_MSG));
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
                final User user = userServiceImpl.getByLogin(login).orElseThrow(
                        () -> new AuthException(Constants.USER_NOT_EXISTS_MSG));
                return getJwtResponse(user);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }


    public JwtResponse registration(UserDto userDto) throws AuthException, NotFoundException {
        Optional<User> userFromDb = userServiceImpl.getByLogin(userDto.getLogin());
        if (userFromDb.isPresent()) {
            throw new AuthException("Пользователь существует");
        }
        userServiceImpl.postUser(userDto);
        userFromDb = userServiceImpl.getByLogin(userDto.getLogin());
        return getJwtResponse(userFromDb.get());
    }

    public JwtResponse changePassword(UserDto userDto) throws NotFoundException, AuthException, BadRequestException {
        if (!AuthServiceCommon.checkAuthorities(userDto.getLogin())) {
            throw new AuthException("Невозможно изменить пароль");
        }
        userServiceImpl.updateUserPassword(userDto);
        Optional<User> user = userServiceImpl.getByLogin(userDto.getLogin());
        return getJwtResponse(user.get());
    }

}