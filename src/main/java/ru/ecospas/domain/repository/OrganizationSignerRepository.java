package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.OrganizationSigner;

import java.util.Optional;

public interface OrganizationSignerRepository
        extends JpaRepository<OrganizationSigner, Integer> {

    Optional<OrganizationSigner> findByOrganizationId(Integer organizationId);

    @Modifying
    @Transactional
    void deleteAllByOrganizationId(Integer organizationId);
}