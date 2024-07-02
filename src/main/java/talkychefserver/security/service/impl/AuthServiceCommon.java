package talkychefserver.security.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import talkychefserver.config.Constants;
import talkychefserver.model.dto.UserDto;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.security.domain.JwtAuthentication;

import java.util.Collection;

@Component
public class AuthServiceCommon {
    public static JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean checkAuthorities(String login) {
        JwtAuthentication principal = getAuthInfo();
        if (principal == null || login == null) {
            return false;
        }
        return isContainsRole(principal.getAuthorities(), "ADMIN") || principal.getLogin().equals(login);
    }

    public static String getUserLogin() {
        JwtAuthentication principal = getAuthInfo();
        if (principal == null) {
            return null;
        }
        return principal.getLogin();
    }

    private static boolean isContainsRole(Collection<? extends GrantedAuthority> authorities, String name) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority() != null && authority.getAuthority().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void checkRegisterConstraints(UserDto dto) {
        if (!isLoginValid(dto.getLogin())) {
            throw new BadRequestException(
                    "Invalid login, must be  " + Constants.LOGIN_MIN_SYMBOLS + "-" + Constants.LOGIN_MAX_SYMBOLS + " "
                            + "symbols, and contain valid symbols");
        }
        if (!isPasswordValid(dto.getPassword())) {
            throw new BadRequestException(
                    "Invalid password, must be  " + Constants.PASSWORD_MIN_SYMBOLS + "-" + Constants.PASSWORD_MAX_SYMBOLS + " symbols, and contain at least 1 digit and 1 non-digit");
        }
        if (!isEmailValid(dto.getEmail())) {
            throw new BadRequestException("Invalid email, must follow rfc 822 & rfc 5322");
        }
    }

    public static boolean isLoginValid(String login) {
        int len = login.length();
        boolean match = login.matches(Constants.LOGIN_PATTERN);
        return len >= Constants.LOGIN_MIN_SYMBOLS && len <= Constants.LOGIN_MAX_SYMBOLS && match;
    }

    public static boolean isPasswordValid(String password) {
        int len = password.length();
        boolean match = password.matches(Constants.PASSWORD_PATTERN);
        return len >= Constants.PASSWORD_MIN_SYMBOLS && len <= Constants.PASSWORD_MAX_SYMBOLS && match;
    }

    public static boolean isEmailValid(String email) {
        return email.matches(Constants.EMAIL_PATTERN);
    }
}
