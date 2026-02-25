package com.caseo.domain.service;

import com.caseo.domain.model.ObjectHazardousSubstance;
import com.caseo.domain.repository.ObjectHazardousSubstanceRepository;

import java.sql.SQLException;

public class ObjectHazardousSubstanceService {

    private final ObjectHazardousSubstanceRepository objectHazardousSubstanceRepository;

    public ObjectHazardousSubstanceService(ObjectHazardousSubstanceRepository objectHazardousSubstanceRepository) {
        this.objectHazardousSubstanceRepository = objectHazardousSubstanceRepository;
    }

    public ObjectHazardousSubstance getById(int id) throws SQLException {
        return objectHazardousSubstanceRepository.findById(id);
    }
}
