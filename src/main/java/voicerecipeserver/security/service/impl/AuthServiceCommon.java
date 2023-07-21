package voicerecipeserver.security.service.impl;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import voicerecipeserver.model.entities.Role;
import voicerecipeserver.security.domain.JwtAuthentication;

import java.util.Collection;
@Component
public class AuthServiceCommon {
    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
    boolean checkAuthorities(String login) {
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
