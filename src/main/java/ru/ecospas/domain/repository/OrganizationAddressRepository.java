package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecospas.domain.model.OrganizationAddress;

import java.util.Optional;

public interface OrganizationAddressRepository
        extends JpaRepository<OrganizationAddress, Integer> {

    Optional<OrganizationAddress> findByOrganizationId(Integer organizationId);
}