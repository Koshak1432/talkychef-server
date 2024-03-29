package talkychefserver.security.service.impl;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import talkychefserver.model.dto.UserDto;
import talkychefserver.model.entities.User;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;
import talkychefserver.respository.UserRepository;
import talkychefserver.security.config.BeanConfig;
import talkychefserver.security.dto.JwtRequest;
import talkychefserver.security.dto.JwtResponse;
import talkychefserver.security.service.AuthService;
import talkychefserver.utils.FindUtils;

import java.util.HashMap;
import java.util.Map;

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

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException, NotFoundException {
        User user = FindUtils.findUserByUid(userRepository, authRequest.getLogin());
        if (passwordEncoder.getPasswordEncoder().matches(authRequest.getPassword(), user.getPassword())) {
            return getJwtResponse(user);
        } else {
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

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws NotFoundException, AuthException {
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
        throw new AuthException("Invalid JWT");
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException, NotFoundException {
        if (!jwtProviderImpl.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = FindUtils.findUserByUid(userRepository, login);
                return getJwtResponse(user);
            }
        }
        throw new AuthException("Invalid JWT");
    }


    public JwtResponse registration(UserDto userDto) throws AuthException, NotFoundException, BadRequestException {
        if (userDto.getLogin() == null) {
            throw new BadRequestException("Login must be present");
        }
        if (userRepository.findByUid(userDto.getLogin()).isPresent()) {
            throw new AuthException("User already exists");
        }
        AuthServiceCommon.checkRegisterConstraints(userDto);
        userServiceImpl.addUser(userDto);
        User user = FindUtils.findUserByUid(userRepository, userDto.getLogin());
        return getJwtResponse(user);
    }

    public JwtResponse changePassword(UserDto userDto) throws NotFoundException, AuthException, BadRequestException {
        if (!AuthServiceCommon.checkAuthorities(userDto.getLogin())) {
            throw new AuthException("No rights");
        }
        userServiceImpl.updateUserPassword(userDto);
        User user = FindUtils.findUserByUid(userRepository, userDto.getLogin());
        return getJwtResponse(user);
    }

}