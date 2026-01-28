package com.caseo.domain.service;

import com.caseo.domain.model.ObjectStructureP1;
import com.caseo.domain.repository.ObjectStructureRepository;

import java.sql.SQLException;
import java.util.List;

public class ObjectStructureService {

    private ObjectStructureRepository objectStructureRepository;

    public ObjectStructureService(ObjectStructureRepository objectStructureRepository) {
        this.objectStructureRepository = objectStructureRepository;
    }

    public List<ObjectStructureP1> getByObject(int objectId) throws SQLException {
        return objectStructureRepository.findByObjectId(objectId);
    }
}

