package com.caseo.domain.service;

import com.caseo.domain.model.User;
import com.caseo.infrastructure.config.HibernateConfig;
import org.hibernate.Session;

public class UserService {

    public User login(String login, String password) {

        User user = findByLogin(login);

        if (user == null) return null;

        if (!user.getPassword().equals(password)) return null;

        return user;
    }

    public User findByLogin(String login) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from User where login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();
        }
    }
}