package com.caseo.domain.service;

import com.caseo.domain.model.ObjectTechnologicalEquipment;
import com.caseo.domain.repository.ObjectTechnologicalEquipmentRepository;

import java.sql.SQLException;
import java.util.List;

public class ObjectTechnologicalEquipmentService {

    private final ObjectTechnologicalEquipmentRepository objectTechnologicalEquipmentRepository;

    public ObjectTechnologicalEquipmentService(ObjectTechnologicalEquipmentRepository objectTechnologicalEquipmentRepository) {
        this.objectTechnologicalEquipmentRepository = objectTechnologicalEquipmentRepository;
    }

    public List<ObjectTechnologicalEquipment> getByObjectId(int objectId) throws SQLException {
        return objectTechnologicalEquipmentRepository.findByObjectId(objectId);
    }
}

