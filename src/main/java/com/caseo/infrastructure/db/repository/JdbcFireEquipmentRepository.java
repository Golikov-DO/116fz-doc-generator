package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.FireEquipment;
import com.caseo.domain.repository.FireEquipmentRepository;

import java.util.List;

public class JdbcFireEquipmentRepository extends BaseJdbcRepository<FireEquipment> implements FireEquipmentRepository {

    @Override
    protected String table() {
        return "primary_fire_extinguishing_equipment";
    }

    @Override
    protected RowMapper<FireEquipment> mapper() {
        return rs -> new FireEquipment(
                rs.getInt("object_id"),
                rs.getInt("num"),
                rs.getString("name_product"),
                rs.getString("quantity"),
                rs.getString("location")
        );
    }

    @Override
    public List<FireEquipment> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }
}