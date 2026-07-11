package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.OrganizationSigner;

import java.util.Optional;

public interface OrganizationSignerRepository
        extends BaseRepository<OrganizationSigner> {

    Optional<OrganizationSigner> findByOrganizationId(Integer organizationId);

    @Modifying
    @Transactional
    void deleteAllByOrganizationId(Integer organizationId);
}