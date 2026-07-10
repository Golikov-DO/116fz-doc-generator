package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.AsfCertificate;

import java.util.Optional;

public interface AsfCertificateRepository
        extends BaseRepository<AsfCertificate> {

    Optional<AsfCertificate> findByAsfId(Integer asfId);
}