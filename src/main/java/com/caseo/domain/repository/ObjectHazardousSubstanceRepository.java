package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectHazardousSubstance;

import java.sql.SQLException;
import java.util.List;

public interface ObjectHazardousSubstanceRepository {

    ObjectHazardousSubstance findById(int id) throws SQLException;

    void save(ObjectHazardousSubstance objectHazardousSubstance, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;

    List<ObjectHazardousSubstance> findAll() throws SQLException;
}

