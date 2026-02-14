package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectFireEquipment;
import com.caseo.domain.repository.ObjectFireEquipmentRepository;

import java.util.List;

public class JdbcObjectFireEquipmentRepository extends BaseJdbcRepository<ObjectFireEquipment> implements ObjectFireEquipmentRepository {

    @Override
    protected String table() {
        return "primary_fire_extinguishing_equipment";
    }

    @Override
    protected RowMapper<ObjectFireEquipment> mapper() {
        return rs -> new ObjectFireEquipment(
                rs.getInt("object_id"),
                rs.getInt("num"),
                rs.getString("name_product"),
                rs.getString("quantity"),
                rs.getString("location")
        );
    }

    @Override
    public List<ObjectFireEquipment> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }
}