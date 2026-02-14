package com.caseo.domain.service;

import com.caseo.domain.model.ObjectFireEquipment;
import com.caseo.domain.repository.ObjectFireEquipmentRepository;

import java.sql.SQLException;
import java.util.List;

public class ObjectFireEquipmentService {

    private final ObjectFireEquipmentRepository objectFireEquipmentRepository;

    public ObjectFireEquipmentService(ObjectFireEquipmentRepository objectFireEquipmentRepository) {
        this.objectFireEquipmentRepository = objectFireEquipmentRepository;
    }

    public List<ObjectFireEquipment> getByObjectId(int objectId) throws SQLException {
        return objectFireEquipmentRepository.findByObjectId(objectId);
    }
}

