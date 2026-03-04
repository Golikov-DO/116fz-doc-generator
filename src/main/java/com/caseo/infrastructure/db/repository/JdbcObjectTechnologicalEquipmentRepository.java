package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectTechnologicalEquipment;
import com.caseo.domain.repository.ObjectTechnologicalEquipmentRepository;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JdbcObjectTechnologicalEquipmentRepository extends BaseJdbcRepository<ObjectTechnologicalEquipment> implements ObjectTechnologicalEquipmentRepository {

    @Override
    protected String table() {
        return "object_technological_equipment";
    }

    @Override
    protected RowMapper<ObjectTechnologicalEquipment> mapper() {
        return rs -> new ObjectTechnologicalEquipment(
                rs.getInt("id"),
                rs.getInt("object_id"),
                rs.getInt("num"),
                rs.getString("equipment_name"),
                rs.getString("characteristics")
        );
    }

    @Override
    public List<ObjectTechnologicalEquipment> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }

    @Override
    public void save(ObjectTechnologicalEquipment objectTechnologicalEquipment, int objectId) throws SQLException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("object_id", objectId);
        data.put("num", objectTechnologicalEquipment.num());
        data.put("equipment_name", objectTechnologicalEquipment.name());
        data.put("characteristics", objectTechnologicalEquipment.characteristics());

        insert(data);
    }

    @Override
    public void deleteByObjectId(int objectId) throws SQLException {
        delete("object_id = ?", objectId);
    }
}