package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectType;

import java.sql.SQLException;

public interface ObjectTypeRepository {

    ObjectType findByObjectId(int objectId);

    void save(ObjectType objectType, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}
