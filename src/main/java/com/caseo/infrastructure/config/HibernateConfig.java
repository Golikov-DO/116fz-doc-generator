package com.caseo.infrastructure.config;

import com.caseo.domain.model.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

public class HibernateConfig {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration config = getConfiguration();

                Class<?>[] entityClasses = {
                        Asf.class,
                        AsfCertificate.class,
                        AsfCompositionDeploymentFunds.class,
                        AsfDocumentImage.class,
                        AsfPersonnel.class,
                        AsfSigner.class,
                        AsfSpecialists.class,
                        AsfWorkType.class,
                        ObjectAccidentScenarios.class,
                        ObjectAddress.class,
                        ObjectCompositionKchs.class,
                        ObjectFireEquipment.class,
                        ObjectHazardousParam.class,
                        ObjectHazardousParamValue.class,
                        ObjectHazardousSubstance.class,
                        ObjectImage.class,
                        ObjectInsurancePolicy.class,
                        ObjectMainScenarios.class,
                        ObjectModel.class,
                        ObjectOrderMinimumBalance.class,
                        ObjectPersonsResponsible.class,
                        ObjectRegionalAuthorities.class,
                        ObjectStructure.class,
                        ObjectTechnologicalBlock.class,
                        ObjectTechnologicalEquipment.class,
                        ObjectType.class,
                        Organization.class,
                        OrganizationAddress.class,
                        OrganizationContact.class,
                        OrganizationSigner.class,
                        ReferenceCity.class,
                        ReferenceEmergencyServices.class,
                        ReferenceTableTitle.class
                };

                for (Class<?> entityClass : entityClasses) {
                    config.addAnnotatedClass(entityClass);
                }

                sessionFactory = config.buildSessionFactory();

            } catch (Exception e) {
                System.err.println(">>> ОШИБКА при создании SessionFactory:");
                e.printStackTrace();
                if (e.getCause() != null) {
                    System.err.println(">>> ПРИЧИНА:");
                    e.getCause().printStackTrace();
                }
                throw new RuntimeException("Ошибка", e);
            }
        }
        return sessionFactory;
    }

    private static @NonNull Configuration getConfiguration() {
        Configuration config = new Configuration();

        config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        config.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/PMLLPA");
        config.setProperty("hibernate.connection.username", "Admin");
        config.setProperty("hibernate.connection.password", "Dimon678");

        config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        config.setProperty("hibernate.show_sql", "false");
        config.setProperty("hibernate.format_sql", "true");
        config.setProperty("hibernate.hbm2ddl.auto", "validate");

        // Волшебная строчка - сама преобразует camelCase в snake_case
        config.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        return config;
    }
}