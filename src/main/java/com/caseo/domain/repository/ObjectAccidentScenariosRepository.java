package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectAccidentScenarios;

import java.sql.SQLException;
import java.util.List;

public interface ObjectAccidentScenariosRepository {

    List<ObjectAccidentScenarios> findByObjectId(int objectId) throws SQLException;

}
