package com.caseo.domain.service;

import com.caseo.domain.model.AccidentScenarios;
import com.caseo.domain.repository.AccidentScenariosRepository;

import java.sql.SQLException;
import java.util.List;

public class AccidentScenariosService {

    private final AccidentScenariosRepository accidentScenariosRepository;

    public AccidentScenariosService(AccidentScenariosRepository accidentScenariosRepository) {
        this.accidentScenariosRepository = accidentScenariosRepository;
    }

    public List<AccidentScenarios> getByObjectId(int objectId) throws SQLException {
        return accidentScenariosRepository.findByObjectId(objectId);
    }
}
