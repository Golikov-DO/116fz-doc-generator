package com.caseo.domain.repository;

import com.caseo.domain.model.MainScenarios;

import java.sql.SQLException;
import java.util.List;

public interface MainScenariosRepository {

    List<MainScenarios> findByObjectId(int objectId) throws SQLException;

}