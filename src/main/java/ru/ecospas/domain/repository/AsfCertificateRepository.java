package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.AsfCertificate;

import java.util.Optional;

public interface AsfCertificateRepository extends JpaRepository<AsfCertificate, Integer> {

    Optional<AsfCertificate> findByAsfId(Integer asfId);
}