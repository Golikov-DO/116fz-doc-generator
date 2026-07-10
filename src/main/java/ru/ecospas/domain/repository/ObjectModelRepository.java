package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectModel;

import java.util.List;

public interface ObjectModelRepository
        extends BaseRepository<ObjectModel> {

    List<ObjectModel> findByOrganizationId(Integer organizationId);
}