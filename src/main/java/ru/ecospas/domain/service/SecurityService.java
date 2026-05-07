package ru.ecospas.domain.service;

import org.hibernate.Session;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.model.User;
import ru.ecospas.infrastructure.config.HibernateConfig;

import java.util.List;

public class SecurityService {

    public List<Organization> getOrganizationsForUser(User user) {

        try (Session session = HibernateConfig.getSessionFactory().openSession()) {

            if (user.getRole().name().equals("ADMIN")) {
                return session.createQuery("from Organization", Organization.class)
                        .list();
            }

            return session.createQuery(
                    "from Organization where user.id = :userId",
                    Organization.class
            )
                    .setParameter("userId", user.getId())
                    .list();
        }
    }

    public boolean hasAccess(User user, Organization org) {
        return user.getRole().name().equals("ADMIN") ||
                org.getUser().getId().equals(user.getId());
    }
}