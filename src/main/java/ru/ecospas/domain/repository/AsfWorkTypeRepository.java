package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.AsfWorkType;

import java.util.List;
import java.util.Optional;

public interface AsfWorkTypeRepository
        extends BaseRepository<AsfWorkType> {

    List<AsfWorkType> findAllByAsfId(Integer asfId);
}