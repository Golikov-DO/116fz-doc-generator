package com.caseo.domain.repository;

import com.caseo.domain.model.TechnologicalEquipment;

import java.sql.SQLException;
import java.util.List;

public interface TechnologicalEquipmentRepository {

    List<TechnologicalEquipment> findByObjectId(int objectId) throws SQLException;

}