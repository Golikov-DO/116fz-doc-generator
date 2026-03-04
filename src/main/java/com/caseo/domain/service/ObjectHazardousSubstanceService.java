package com.caseo.domain.service;

import com.caseo.domain.model.ObjectHazardousSubstance;
import com.caseo.domain.repository.ObjectHazardousSubstanceRepository;

import java.sql.SQLException;
import java.util.List;

public class ObjectHazardousSubstanceService {

    private final ObjectHazardousSubstanceRepository objectHazardousSubstanceRepository;

    public ObjectHazardousSubstanceService(ObjectHazardousSubstanceRepository objectHazardousSubstanceRepository) {
        this.objectHazardousSubstanceRepository = objectHazardousSubstanceRepository;
    }

    public ObjectHazardousSubstance getById(int id) throws SQLException {
        return objectHazardousSubstanceRepository.findById(id);
    }

    public void save(ObjectHazardousSubstance objectHazardousSubstance, int objectId) throws SQLException{
        objectHazardousSubstanceRepository.save(objectHazardousSubstance, objectId);
    }

    public void deleteByObjectId (int objectId) throws SQLException{
        objectHazardousSubstanceRepository.deleteByObjectId(objectId);
    }

    public List<ObjectHazardousSubstance> getAll() throws SQLException {
        return objectHazardousSubstanceRepository.findAll();
    }
}
