package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectMainScenarios;

import java.sql.SQLException;
import java.util.List;

public interface ObjectMainScenariosRepository {

    List<ObjectMainScenarios> findByObjectId(int objectId) throws SQLException;

    void save(ObjectMainScenarios objectMainScenarios, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}