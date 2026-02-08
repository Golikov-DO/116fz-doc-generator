package com.caseo.domain.service;

import com.caseo.domain.model.TechnologicalEquipment;
import com.caseo.domain.repository.TechnologicalEquipmentRepository;

import java.sql.SQLException;
import java.util.List;

public class TechnologicalEquipmentService {

    private final TechnologicalEquipmentRepository technologicalEquipmentRepository;

    public TechnologicalEquipmentService(TechnologicalEquipmentRepository technologicalEquipmentRepository) {
        this.technologicalEquipmentRepository = technologicalEquipmentRepository;
    }

    public List<TechnologicalEquipment> getByObject(int objectId) throws SQLException {
        return technologicalEquipmentRepository.findByObjectId(objectId);
    }
}

