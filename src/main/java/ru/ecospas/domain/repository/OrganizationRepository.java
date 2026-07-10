package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.Organization;

import java.util.List;

public interface OrganizationRepository
        extends BaseRepository<Organization> {

    List<Organization> findByUserId(Integer userId);
}