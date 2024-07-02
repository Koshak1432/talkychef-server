package talkychefserver.security.service.impl;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import talkychefserver.model.dto.UserDto;
import talkychefserver.model.entities.User;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.respositories.UserRepository;
import talkychefserver.security.config.BeanConfig;
import talkychefserver.security.dto.JwtRequest;
import talkychefserver.security.dto.JwtResponse;
import talkychefserver.security.service.AuthService;
import talkychefserver.utils.FindUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImplMobile implements AuthService {
    private final BeanConfig passwordEncoder;
    private final UserServiceImpl userServiceImpl;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProviderImpl jwtProviderImpl;
    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImplMobile(BeanConfig passwordEncoder, UserServiceImpl userServiceImpl,
                                 JwtProviderImpl jwtProviderImpl, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userServiceImpl = userServiceImpl;
        this.jwtProviderImpl = jwtProviderImpl;
        this.userRepository = userRepository;
    }

    @Override
    public JwtResponse login(@NonNull JwtRequest authRequest) {
        log.info("Processing login request");
        User user = FindUtils.findUserByUid(userRepository, authRequest.getLogin());
        if (passwordEncoder.getPasswordEncoder().matches(authRequest.getPassword(), user.getPassword())) {
            return getJwtResponse(user);
        } else {
            log.error("Couldn't login: wrong password");
            throw new AuthException("Wrong password");
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

    @Override
    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        log.info("Processing get access token request");
        if (jwtProviderImpl.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = FindUtils.findUserByUid(userRepository, login);
                final String accessToken = jwtProviderImpl.generateAccessToken(user);
                JwtResponse jwtResponse = new JwtResponse();
                jwtResponse.accessToken(accessToken);
                return jwtResponse;
            }
        }
        log.error("Validating refresh token failed");
        throw new AuthException("Invalid JWT");
    }

    @Override
    public JwtResponse refresh(@NonNull String refreshToken) {
        log.info("Processing refresh request");
        if (!jwtProviderImpl.validateRefreshToken(refreshToken)) {
            log.error("Validating refresh token failed");
            throw new AuthException("Invalid JWT");
        }
        final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
        final String login = claims.getSubject();
        final String saveRefreshToken = refreshStorage.get(login);
        if (saveRefreshToken == null || !saveRefreshToken.equals(refreshToken)) {
            throw new AuthException("Invalid JWT");
        }
        User user = FindUtils.findUserByUid(userRepository, login);
        return getJwtResponse(user);
    }


    @Override
    public JwtResponse registration(UserDto userDto) {
        log.info("Processing registration request");
        AuthServiceCommon.checkRegisterConstraints(userDto);
        userServiceImpl.addUser(userDto);
        User user = FindUtils.findUserByUid(userRepository, userDto.getLogin());
        return getJwtResponse(user);
    }

    @Override
    public JwtResponse changePassword(UserDto userDto) {
        User user = userServiceImpl.updateUserPassword(userDto);
        return getJwtResponse(user);
    }

}