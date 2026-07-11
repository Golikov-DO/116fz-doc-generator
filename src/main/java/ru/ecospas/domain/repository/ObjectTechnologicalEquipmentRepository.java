package ru.ecospas.domain.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ObjectTechnologicalEquipment;

import java.util.List;

public interface ObjectTechnologicalEquipmentRepository
        extends BaseRepository<ObjectTechnologicalEquipment> {

    List<ObjectTechnologicalEquipment> findAllByObjectId(Integer objectId);

    @Modifying
    @Transactional
    void deleteAllByObjectId(Integer objectId);
}