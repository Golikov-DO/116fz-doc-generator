package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.OrganizationSigner;

import java.util.List;

public interface OrganizationSignerRepository
        extends BaseRepository<OrganizationSigner> {

    List<OrganizationSigner> findAllByOrganizationId(Integer organizationId);
}