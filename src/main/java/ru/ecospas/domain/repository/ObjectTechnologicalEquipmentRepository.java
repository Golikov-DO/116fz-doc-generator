package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectTechnologicalEquipment;

import java.util.List;

public interface ObjectTechnologicalEquipmentRepository
        extends BaseRepository<ObjectTechnologicalEquipment> {

    List<ObjectTechnologicalEquipment> findAllByObjectId(Integer objectId);
}