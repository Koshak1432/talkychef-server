package voicerecipeserver.security.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.security.config.BeanConfig;
import voicerecipeserver.security.dto.JwtRequest;
import voicerecipeserver.security.dto.JwtResponse;
import voicerecipeserver.security.service.AuthService;
import voicerecipeserver.utils.FindUtils;

@Service

public class AuthServiceImplWeb implements AuthService {

    @Autowired
    public AuthServiceImplWeb(BeanConfig passwordEncoder, UserServiceImpl userServiceImpl,
                              JwtProviderImpl jwtProviderImpl, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userServiceImpl = userServiceImpl;
        this.jwtProviderImpl = jwtProviderImpl;
        this.userRepository = userRepository;
    }

    private final BeanConfig passwordEncoder;
    private final UserServiceImpl userServiceImpl;
    private final JwtProviderImpl jwtProviderImpl;
    private final UserRepository userRepository;

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException, NotFoundException {
        User user = FindUtils.findUserByUid(userRepository, authRequest.getLogin());
        if (passwordEncoder.getPasswordEncoder().matches(authRequest.getPassword(), user.getPassword())) {
            return getJwtResponseAndFillCookie(user);
        } else {
            throw new AuthException("Wrong password");
        }
    }


    public JwtResponse getAccessToken(@NonNull String refreshToken) throws NotFoundException {
        JwtResponse jwtResponse = new JwtResponse();
        if (jwtProviderImpl.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            User user = FindUtils.findUserByUid(userRepository, login);
            final String accessToken = jwtProviderImpl.generateAccessToken(user);
            jwtResponse.setAccessToken(accessToken);
        }
        return jwtResponse;
    }


    private static void fillRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days in seconds
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
    }

    public JwtResponse refresh(@CookieValue(value = "refreshToken") @NonNull String refreshToken) throws AuthException,
            NotFoundException {
        if (!jwtProviderImpl.validateRefreshToken(refreshToken)) {
            throw new AuthException("Invalid JWT");
        }
        final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
        final String login = claims.getSubject();
        User user = FindUtils.findUserByUid(userRepository, login);
        return getJwtResponseAndFillCookie(user);
    }


    public JwtResponse registration(UserDto userDto) throws AuthException, NotFoundException, BadRequestException {
        if (userDto.getLogin() == null) {
            throw new BadRequestException("Login must be present");
        }
        if (userRepository.findByUid(userDto.getLogin()).isPresent()) {
            throw new AuthException("User already exists");
        }
        userServiceImpl.postUser(userDto);
        User user = FindUtils.findUserByUid(userRepository, userDto.getLogin());
        return getJwtResponseAndFillCookie(user);
    }

    public JwtResponse changePassword(UserDto userDto) throws NotFoundException, AuthException, BadRequestException {
        if (!AuthServiceCommon.checkAuthorities(userDto.getLogin())) {
            throw new AuthException("No rights");
        }
        userServiceImpl.updateUserPassword(userDto);
        User user = FindUtils.findUserByUid(userRepository, userDto.getLogin());
        return getJwtResponseAndFillCookie(user);

    }

    private JwtResponse getJwtResponse(User user) {
        final String accessToken = jwtProviderImpl.generateAccessToken(user);
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.accessToken(accessToken);
        return jwtResponse;
    }

    private JwtResponse getJwtResponseAndFillCookie(User user) {
        JwtResponse jwtResponse;
        jwtResponse = getJwtResponse(user);
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletResponse response = requestAttributes.getResponse();
            if (response != null) {
                final String refreshToken = jwtProviderImpl.generateRefreshToken(user);
                fillRefreshTokenCookie(response, refreshToken);
            }
        }
        return jwtResponse;
    }


    public String getRefreshFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}