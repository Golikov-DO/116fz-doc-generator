package ru.ecospas.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@Import(AbstractIntegrationTest.TestContainersConfig.class)
public abstract class AbstractIntegrationTest {

    @TestConfiguration(proxyBeanMethods = false)
    static class TestContainersConfig {

        @Bean
        @ServiceConnection
        PostgreSQLContainer<?> postgresContainer() {
            return new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("ecospas")
                    .withUsername("postgres")
                    .withPassword("postgres");
        }
    }
}