package com.caseo.domain.service;

import com.caseo.domain.model.Organization;
import com.caseo.domain.model.User;
import com.caseo.infrastructure.config.HibernateConfig;
import org.hibernate.Session;

import java.util.List;

public class OrganizationSecurityService {

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
}