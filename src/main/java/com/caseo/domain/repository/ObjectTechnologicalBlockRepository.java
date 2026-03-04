package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectTechnologicalBlock;

import java.sql.SQLException;
import java.util.List;

public interface ObjectTechnologicalBlockRepository {

    List<ObjectTechnologicalBlock> findByObjectId(int objectId) throws SQLException;

    int countByObjectId(int objectId) throws SQLException;

    void save(ObjectTechnologicalBlock objectTechnologicalBlock, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}
