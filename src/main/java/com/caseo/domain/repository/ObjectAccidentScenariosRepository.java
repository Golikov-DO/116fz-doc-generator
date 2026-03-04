package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectAccidentScenarios;

import java.sql.SQLException;
import java.util.List;

public interface ObjectAccidentScenariosRepository {

    List<ObjectAccidentScenarios> findByObjectId(int objectId) throws SQLException;

    void save(ObjectAccidentScenarios objectAccidentScenarios, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}
