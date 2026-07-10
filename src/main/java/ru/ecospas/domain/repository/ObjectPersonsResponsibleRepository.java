package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectPersonsResponsible;

import java.util.Optional;

public interface ObjectPersonsResponsibleRepository
        extends BaseRepository<ObjectPersonsResponsible> {

    Optional<ObjectPersonsResponsible> findByObjectId(Integer objectId);
}