package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectTechnologicalEquipment;

import java.sql.SQLException;
import java.util.List;

public interface ObjectTechnologicalEquipmentRepository {

    List<ObjectTechnologicalEquipment> findByObjectId(int objectId) throws SQLException;

    void save(ObjectTechnologicalEquipment objectTechnologicalEquipment, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}