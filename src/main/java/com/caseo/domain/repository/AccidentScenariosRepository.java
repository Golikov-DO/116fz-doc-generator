package com.caseo.domain.repository;

import com.caseo.domain.model.AccidentScenarios;

import java.sql.SQLException;
import java.util.List;

public interface AccidentScenariosRepository {

    List<AccidentScenarios> findByObjectId(int objectId) throws SQLException;

}
