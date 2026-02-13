package com.caseo.domain.service;

import com.caseo.domain.model.FireEquipment;
import com.caseo.domain.repository.FireEquipmentRepository;

import java.sql.SQLException;
import java.util.List;

public class FireEquipmentService {

    private final FireEquipmentRepository fireEquipmentRepository;

    public FireEquipmentService(FireEquipmentRepository fireEquipmentRepository) {
        this.fireEquipmentRepository = fireEquipmentRepository;
    }

    public List<FireEquipment> getByObjectId(int objectId) throws SQLException {
        return fireEquipmentRepository.findByObjectId(objectId);
    }
}

