package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectType;
import com.caseo.domain.repository.ObjectTypeRepository;

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
}
