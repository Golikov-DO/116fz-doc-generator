package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectTechnologicalBlock;

import java.util.List;

public interface ObjectTechnologicalBlockRepository
        extends BaseRepository<ObjectTechnologicalBlock> {

    List<ObjectTechnologicalBlock> findAllByObjectId(Integer objectId);
}