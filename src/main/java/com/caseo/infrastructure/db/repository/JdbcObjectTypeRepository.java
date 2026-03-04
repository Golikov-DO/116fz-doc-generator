package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectType;
import com.caseo.domain.repository.ObjectTypeRepository;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcObjectTypeRepository extends BaseJdbcRepository<ObjectType> implements ObjectTypeRepository {

    @Override
    protected String table() {
        return "object_type";
    }

    @Override
    protected RowMapper<ObjectType> mapper() {
        return rs -> new ObjectType(
                rs.getInt("id"),
                rs.getInt("object_id"),
                rs.getString("object_type_definitions")
        );
    }

    @Override
    public ObjectType findByObjectId(int objectId) {
        return findOne("object_id = ?", objectId).orElse(null);
    }

    @Override
    public void save(ObjectType objectType, int objectId) throws SQLException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("object_id", objectId);
        data.put("object_type_definitions", objectType.typeDefinition());

        insert(data);
    }

    @Override
    public void deleteByObjectId(int objectId) throws SQLException {
        delete("object_id = ?", objectId);
    }
}
