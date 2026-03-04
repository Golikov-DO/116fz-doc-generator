package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectFireEquipment;

import java.sql.SQLException;
import java.util.List;

public interface ObjectFireEquipmentRepository {

    List<ObjectFireEquipment> findByObjectId(int objectId) throws SQLException;

    void save(ObjectFireEquipment objectFireEquipment, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}