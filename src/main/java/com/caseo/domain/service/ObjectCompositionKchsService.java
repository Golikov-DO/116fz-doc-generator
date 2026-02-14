package com.caseo.domain.service;

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
}

