package ru.ecospas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.ecospas.domain.repository.BaseRepositoryImpl;

@ServletComponentScan
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
@SpringBootApplication
public class EcospasApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcospasApplication.class, args);
    }


}
