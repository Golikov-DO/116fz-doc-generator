package com.caseo.domain.service;

import com.caseo.domain.model.MainScenarios;
import com.caseo.domain.repository.MainScenariosRepository;

import java.sql.SQLException;
import java.util.List;

public class MainScenariosService {

    private final MainScenariosRepository mainScenariosRepository;

    public MainScenariosService(MainScenariosRepository mainScenariosRepository) {
        this.mainScenariosRepository = mainScenariosRepository;
    }

    public List<MainScenarios> getByObjectId(int objectId) throws SQLException {
        return mainScenariosRepository.findByObjectId(objectId);
    }
}

