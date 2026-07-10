package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectStructure;

import java.util.List;

public interface ObjectStructureRepository
        extends BaseRepository<ObjectStructure> {

    List<ObjectStructure> findAllByObjectId(Integer objectId);
}