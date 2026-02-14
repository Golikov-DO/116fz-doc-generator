package com.caseo.domain.service;

import com.caseo.domain.model.ObjectMainScenarios;
import com.caseo.domain.repository.ObjectMainScenariosRepository;

import java.sql.SQLException;
import java.util.List;

public class ObjectMainScenariosService {

    private final ObjectMainScenariosRepository objectMainScenariosRepository;

    public ObjectMainScenariosService(ObjectMainScenariosRepository objectMainScenariosRepository) {
        this.objectMainScenariosRepository = objectMainScenariosRepository;
    }

    public List<ObjectMainScenarios> getByObjectId(int objectId) throws SQLException {
        return objectMainScenariosRepository.findByObjectId(objectId);
    }
}

