package com.caseo.infrastructure.config;

import com.caseo.domain.model.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;

public class HibernateConfig {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration config = getConfiguration();

                // АВТОПОИСК
                scanAndAddEntities(config);

                sessionFactory = config.buildSessionFactory();

            } catch (Exception e) {
                throw new RuntimeException("Критический сбой Hibernate: " + e.getMessage(), e);
            }
        }
        return sessionFactory;
    }

    private static void scanAndAddEntities(Configuration config) throws Exception {
        String packageName = "com.caseo.domain.model";
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);

        while (resources.hasMoreElements()) {
            File directory = new File(resources.nextElement().getFile());
            if (directory.exists() && directory.isDirectory()) {
                String[] files = directory.list();
                if (files != null) {
                    for (String file : files) {
                        if (file.endsWith(".class")) {
                            String className = packageName + "." + file.substring(0, file.length() - 6);
                            Class<?> clazz = Class.forName(className);

                            // ФИЛЬТР: Добавляем только те классы, над которыми стоит @Entity
                            if (clazz.isAnnotationPresent(jakarta.persistence.Entity.class)) {
                                config.addAnnotatedClass(clazz);
                            }
                        }
                    }
                }
            }
        }
    }

    private static @NonNull Configuration getConfiguration() {
        Configuration config = new Configuration();

        config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        config.setProperty("hibernate.connection.url", "jdbc:postgresql://maglev.proxy.rlwy.net:43448/railway");
        config.setProperty("hibernate.connection.username", "postgres");
        config.setProperty("hibernate.connection.password", "SWtVkqzxdUqWVpJxwnybRXxDfvJCJTna");
//        config.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/PMLLPA");
//        config.setProperty("hibernate.connection.username", "Admin");
//        config.setProperty("hibernate.connection.password", "Dimon678");

        config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        config.setProperty("hibernate.show_sql", "false");
        config.setProperty("hibernate.format_sql", "true");
        config.setProperty("hibernate.hbm2ddl.auto", "validate");

        config.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        return config;
    }
}
