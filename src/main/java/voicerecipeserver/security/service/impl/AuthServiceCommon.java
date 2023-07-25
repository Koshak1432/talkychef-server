package voicerecipeserver.security.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import voicerecipeserver.security.domain.JwtAuthentication;

import java.util.Collection;
@Component
public class AuthServiceCommon {
    public static JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
    public static boolean checkAuthorities(String login) {
        JwtAuthentication principal = getAuthInfo();
        if (principal == null) {
            return false;
        }
        return isContainsRole(principal.getAuthorities(), "ADMIN") || principal.getLogin().equals(login);
    }

    private static boolean isContainsRole(Collection<? extends GrantedAuthority> authorities, String name) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority() != null && authority.getAuthority().equals(name)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isSamePerson(String userUid) {
        JwtAuthentication principal = AuthServiceCommon.getAuthInfo();
        return principal.getLogin().equals(userUid);
    }
}
