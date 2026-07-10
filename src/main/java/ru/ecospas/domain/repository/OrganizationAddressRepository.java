package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.OrganizationAddress;

import java.util.Optional;

public interface OrganizationAddressRepository
        extends BaseRepository<OrganizationAddress> {

    Optional<OrganizationAddress> findByOrganizationId(Integer organizationId);
}