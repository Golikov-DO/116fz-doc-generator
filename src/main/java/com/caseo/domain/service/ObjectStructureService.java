package com.caseo.domain.service;

import com.caseo.domain.model.ObjectStructure;
import com.caseo.domain.repository.ObjectStructureRepository;

import java.sql.SQLException;
import java.util.List;

public class ObjectStructureService {

    private final ObjectStructureRepository objectStructureRepository;

    public ObjectStructureService(ObjectStructureRepository objectStructureRepository) {
        this.objectStructureRepository = objectStructureRepository;
    }

    public List<ObjectStructure> getByObjectId(int objectId) throws SQLException {
        return objectStructureRepository.findByObjectId(objectId);
    }
}

