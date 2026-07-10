package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectHazardousParamValue;

import java.util.List;

public interface ObjectHazardousParamValueRepository
        extends BaseRepository<ObjectHazardousParamValue> {

    List<ObjectHazardousParamValue> findAllBySubstanceId(Integer substanceId);
}