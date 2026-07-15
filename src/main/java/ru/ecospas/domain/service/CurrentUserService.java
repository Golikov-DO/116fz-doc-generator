package ru.ecospas.domain.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.infrastructure.security.UserPrincipal;

@Service
public class CurrentUserService {

    public User currentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return null;
        }

        return principal.getUser();
    }

    public boolean isAuthenticated() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserPrincipal;
    }

    public boolean isAdmin() {
        User user = currentUser();
        return user != null && user.getRole() == Role.ADMIN;
    }
    public Integer currentUserId() {
        User user = currentUser();
        return user != null ? user.getId() : null;
    }

}