package com.caseo.domain.repository;

import com.caseo.domain.model.FireEquipment;

import java.sql.SQLException;
import java.util.List;

public interface FireEquipmentRepository {

    List<FireEquipment> findByObjectId(int objectId) throws SQLException;

}