package com.caseo.domain.service;

import com.caseo.domain.model.ObjectType;
import com.caseo.domain.repository.ObjectTypeRepository;

import java.sql.SQLException;

public class ObjectTypeService {

    private final ObjectTypeRepository objectTypeRepository;

    public ObjectTypeService(ObjectTypeRepository objectTypeRepository) {
        this.objectTypeRepository = objectTypeRepository;
    }

    public ObjectType getObjectType(int id) {
        return objectTypeRepository.findByObjectId(id);
    }

    public void save(ObjectType objectType, int objectId) throws SQLException {
        objectTypeRepository.save(objectType, objectId);
    }

    public void deleteByObjectId (int objectId) throws SQLException{
        objectTypeRepository.deleteByObjectId(objectId);
    }
}
