package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.TechnologicalEquipment;
import com.caseo.domain.repository.TechnologicalEquipmentRepository;

import java.util.List;

public class JdbcTechnologicalEquipmentRepository extends BaseJdbcRepository<TechnologicalEquipment> implements TechnologicalEquipmentRepository {

    @Override
    protected String table() {
        return "technological_equipment";
    }

    @Override
    protected RowMapper<TechnologicalEquipment> mapper() {
        return rs -> new TechnologicalEquipment(
                        rs.getInt("id"),
                        rs.getInt("num"),
                        rs.getString("equipment_name"),
                        rs.getString("characteristics"),
                        rs.getInt("object_id")
        );
    }

    @Override
    public List<TechnologicalEquipment> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }
}