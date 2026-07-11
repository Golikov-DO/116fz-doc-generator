package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.Role;
import ru.ecospas.domain.model.User;
import ru.ecospas.domain.repository.OrganizationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityService {

    private final OrganizationRepository repository;

    public List<Organization> getOrganizationsForUser(User user) {

        if (user.getRole() == Role.ADMIN) {
            return repository.findAll();
        }

        return repository.findByUserId(user.getId());
    }

    public boolean hasAccess(User user, Organization organization) {

        return user.getRole() == Role.ADMIN
                || organization.getUser().getId().equals(user.getId());
    }
}