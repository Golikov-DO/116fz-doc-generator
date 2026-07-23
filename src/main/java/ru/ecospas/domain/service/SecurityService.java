package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityService {

    public boolean hasAccess(User user, Organization organization) {

        return user.getRole() == Role.ADMIN
                || organization.getUser().getId().equals(user.getId());
    }
}