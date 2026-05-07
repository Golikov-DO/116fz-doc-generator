package ru.ecospas.domain.service;

import org.hibernate.Session;
import ru.ecospas.domain.model.User;
import ru.ecospas.infrastructure.config.HibernateConfig;

import java.util.List;

public class UserAdminService {

    public List<User> getAllUsers() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    public void save(User user) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();

            if (user.getId() == null) {
                session.persist(user);
            } else {
                session.merge(user);
            }

            session.getTransaction().commit();
        }
    }

    public void delete(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }

            session.getTransaction().commit();
        }
    }

    public User findById(int id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    public User findByLogin(String login) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {

            return session.createQuery(
                            "from User where login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();
        }
    }

    public User login(String login, String password) {
        User user = findByLogin(login);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }
}