package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectStructure;

import java.sql.SQLException;
import java.util.List;

public interface ObjectStructureRepository {

    List<ObjectStructure> findByObjectId(int objectId) throws SQLException;

    void save(ObjectStructure objectStructure, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}