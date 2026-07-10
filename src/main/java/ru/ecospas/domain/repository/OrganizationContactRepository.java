package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.OrganizationContact;

import java.util.List;

public interface OrganizationContactRepository
        extends BaseRepository<OrganizationContact> {

    List<OrganizationContact> findAllByOrganizationId(Integer organizationId);
}