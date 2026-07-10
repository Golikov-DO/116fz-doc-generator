package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectFireEquipment;

import java.util.Optional;

public interface ObjectFireEquipmentRepository
        extends BaseRepository<ObjectFireEquipment> {

    Optional<ObjectFireEquipment> findByObjectId(Integer objectId);
}