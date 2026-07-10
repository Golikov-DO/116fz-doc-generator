package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.AsfSpecialists;

import java.util.Optional;

public interface AsfSpecialistsRepository
        extends BaseRepository<AsfSpecialists> {

    Optional<AsfSpecialists> findByAsfId(Integer asfId);
}