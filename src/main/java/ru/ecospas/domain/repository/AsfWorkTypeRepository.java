package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.AsfWorkType;

import java.util.Optional;

public interface AsfWorkTypeRepository
        extends BaseRepository<AsfWorkType> {

    Optional<AsfWorkType> findByAsfId(Integer asfId);
}