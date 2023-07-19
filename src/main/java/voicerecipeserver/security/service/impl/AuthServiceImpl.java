package voicerecipeserver.security.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.entities.Role;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.security.config.BeanConfig;
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.security.dto.JwtRequest;
import voicerecipeserver.security.dto.JwtResponse;
import voicerecipeserver.security.service.AuthService;

import java.util.Collection;
import java.util.Optional;

@Service

public class AuthServiceImpl implements AuthService {
    public static final String USER_NOT_EXISTS = "Пользователь не найден";
    
    @Autowired
    public AuthServiceImpl(BeanConfig passwordEncoder, UserServiceImpl userServiceImpl,
                           JwtProviderImpl jwtProviderImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userServiceImpl = userServiceImpl;
        this.jwtProviderImpl = jwtProviderImpl;
    }

    private final BeanConfig passwordEncoder;
    private final UserServiceImpl userServiceImpl;
    private final JwtProviderImpl jwtProviderImpl;

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {

        final User user = userServiceImpl.getByLogin(authRequest.getLogin()).orElseThrow(
                () -> new AuthException(USER_NOT_EXISTS));
        if (passwordEncoder.getPasswordEncoder().matches(authRequest.getPassword(), user.getPassword())) {
            return getJwtResponseAndFillCookie(user);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }



    public JwtResponse getAccessToken(@CookieValue(value = "refreshToken", required = true) @NonNull String refreshToken) throws AuthException {
        JwtResponse jwtResponse = new JwtResponse();
        if (jwtProviderImpl.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final User user = userServiceImpl.getByLogin(login).orElseThrow(
                    () -> new AuthException(USER_NOT_EXISTS));
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

    public JwtResponse refresh(@CookieValue(value = "refreshToken", required = true) @NonNull String refreshToken) throws AuthException {
        if (jwtProviderImpl.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final User user = userServiceImpl.getByLogin(login).orElseThrow(
                    () -> new AuthException(USER_NOT_EXISTS));
            return getJwtResponseAndFillCookie(user);
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public JwtResponse registration(UserDto userDto) throws AuthException, NotFoundException {
        Optional<User> userFromDb = userServiceImpl.getByLogin(userDto.getLogin());
        if (userFromDb.isPresent()) {
            throw new AuthException("Пользователь существует");
        }
        userServiceImpl.postUser(userDto);
        userFromDb = userServiceImpl.getByLogin(userDto.getLogin());
        return userFromDb.map(this::getJwtResponseAndFillCookie).orElseGet(JwtResponse::new);
    }

    public JwtResponse changePassword(UserDto userDto) throws NotFoundException, AuthException {
        if (!checkAuthorities(userDto.getLogin())) {
            throw new AuthException("Невозможно изменить пароль");
        }
        userServiceImpl.updateUserPassword(userDto);
        Optional<User> user = userServiceImpl.getByLogin(userDto.getLogin());
        return user.map(this::getJwtResponseAndFillCookie).orElseGet(JwtResponse::new);

    }

    private JwtResponse getJwtResponse(User user) {
        final String accessToken = jwtProviderImpl.generateAccessToken(user);
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.accessToken(accessToken);
        return jwtResponse;
    }

    private JwtResponse getJwtResponseAndFillCookie(User user) {
        JwtResponse jwtResponse = null;
             jwtResponse = getJwtResponse(user);
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
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
    private boolean checkAuthorities(String login) {
        JwtAuthentication principal = getAuthInfo();
        return isContainsRole(principal.getRoles(), "ADMIN") || principal.getLogin().equals(login);
    }

    private static boolean isContainsRole(Collection<Role> roles, String roleName) {
        for (Role role : roles) {
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}