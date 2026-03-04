package com.caseo.domain.service;

import com.caseo.domain.model.ObjectTechnologicalBlock;
import com.caseo.domain.repository.ObjectTechnologicalBlockRepository;

import java.sql.SQLException;
import java.util.List;

public class ObjectTechnologicalBlockService {

    private final ObjectTechnologicalBlockRepository objectTechnologicalBlockRepository;

    public ObjectTechnologicalBlockService(ObjectTechnologicalBlockRepository objectTechnologicalBlockRepository) {
        this.objectTechnologicalBlockRepository = objectTechnologicalBlockRepository;
    }

    public List<ObjectTechnologicalBlock> getByObject(int objectId) throws SQLException {
        return objectTechnologicalBlockRepository.findByObjectId(objectId);
    }

    public int countByObjectId(int objectId) throws SQLException {
        return objectTechnologicalBlockRepository.countByObjectId(objectId);
    }

    public void save(ObjectTechnologicalBlock objectTechnologicalBlock, int objectId) throws SQLException{
        objectTechnologicalBlockRepository.save(objectTechnologicalBlock, objectId);
    }

    public void deleteByObjectId (int objectId) throws SQLException{
        objectTechnologicalBlockRepository.deleteByObjectId(objectId);
    }
}
