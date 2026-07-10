package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.AsfPersonnel;

import java.util.Optional;

public interface AsfPersonnelRepository
        extends BaseRepository<AsfPersonnel> {

    Optional<AsfPersonnel> findByAsfId(Integer asfId);
}