package com.caseo.infrastructure.db;

import com.caseo.infrastructure.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Consumer;
import java.util.function.Function;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

    // Для чтения данных (уже есть)
    public static <R> R inSession(Function<Session, R> action) {
        try (Session session = sessionFactory.openSession()) {
            return action.apply(session);
        }
    }

    // ДОБАВЛЯЕМ для сохранения/удаления
    public static void inTransaction(Consumer<Session> action) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                action.accept(session);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }
}