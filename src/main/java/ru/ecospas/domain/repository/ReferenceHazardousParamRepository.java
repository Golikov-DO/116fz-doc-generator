package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ReferenceHazardousParam;

import java.util.List;

public interface ReferenceHazardousParamRepository
        extends BaseRepository<ReferenceHazardousParam> {

    List<ReferenceHazardousParam> findAll();
}