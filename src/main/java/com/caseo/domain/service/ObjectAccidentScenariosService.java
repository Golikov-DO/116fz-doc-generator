package com.caseo.domain.service;

import com.caseo.domain.model.ObjectAccidentScenarios;
import com.caseo.domain.repository.ObjectAccidentScenariosRepository;

import java.sql.SQLException;
import java.util.List;

public class ObjectAccidentScenariosService {

    private final ObjectAccidentScenariosRepository objectAccidentScenariosRepository;

    public ObjectAccidentScenariosService(ObjectAccidentScenariosRepository objectAccidentScenariosRepository) {
        this.objectAccidentScenariosRepository = objectAccidentScenariosRepository;
    }

    public List<ObjectAccidentScenarios> getByObjectId(int objectId) throws SQLException {
        return objectAccidentScenariosRepository.findByObjectId(objectId);
    }
}
