package ru.ecospas.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Docx4jConfiguration {

    private static final String DOCX4J_TMP_DIR = "docx4j.tmpdir";

    @PostConstruct
    public void configureTemporaryDirectory() {
        System.setProperty(
                DOCX4J_TMP_DIR,
                System.getProperty("java.io.tmpdir")
        );
    }
}