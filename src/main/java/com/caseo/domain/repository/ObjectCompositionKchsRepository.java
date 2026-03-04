package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectCompositionKchs;

import java.sql.SQLException;
import java.util.List;

public interface ObjectCompositionKchsRepository {

    List<ObjectCompositionKchs> findByObjectId(int objectId) throws SQLException;

    void save(ObjectCompositionKchs objectCompositionKchs, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}