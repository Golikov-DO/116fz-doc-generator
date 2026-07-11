package ru.ecospas.domain.repository;

import ru.ecospas.domain.model.ObjectFireEquipment;

import java.util.List;

public interface ObjectFireEquipmentRepository
        extends BaseRepository<ObjectFireEquipment> {

    List<ObjectFireEquipment> findAllByObjectIdOrderByNumber(Integer objectId);

}