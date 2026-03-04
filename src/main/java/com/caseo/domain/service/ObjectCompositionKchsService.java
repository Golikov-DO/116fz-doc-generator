package com.caseo.domain.service;

import com.caseo.domain.model.ObjectAddress;
import com.caseo.domain.model.ObjectCompositionKchs;
import com.caseo.domain.repository.ObjectCompositionKchsRepository;

import java.sql.SQLException;
import java.util.List;

public class ObjectCompositionKchsService {

    private final ObjectCompositionKchsRepository objectCompositionKchsRepository;

    public ObjectCompositionKchsService(ObjectCompositionKchsRepository objectCompositionKchsRepository) {
        this.objectCompositionKchsRepository = objectCompositionKchsRepository;
    }

    public List<ObjectCompositionKchs> getByObjectId(int objectId) throws SQLException {
        return objectCompositionKchsRepository.findByObjectId(objectId);
    }

    public void save(ObjectCompositionKchs objectCompositionKchs, int objectId) throws SQLException{
        objectCompositionKchsRepository.save(objectCompositionKchs, objectId);
    }

    public void deleteByObjectId (int objectId) throws SQLException{
        objectCompositionKchsRepository.deleteByObjectId(objectId);
    }
}

