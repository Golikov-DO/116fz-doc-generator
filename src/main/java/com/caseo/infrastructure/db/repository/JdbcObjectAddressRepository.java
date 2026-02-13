package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectAddress;
import com.caseo.domain.repository.ObjectAddressRepository;

public class JdbcObjectAddressRepository extends BaseJdbcRepository<ObjectAddress> implements ObjectAddressRepository {

    @Override
    protected String table() {
        return "object_address";
    }

    @Override
    protected RowMapper<ObjectAddress> mapper() {
        return rs -> new ObjectAddress(
                rs.getInt("id"),
                rs.getLong("object_id"),
                rs.getObject("index") != null ? rs.getInt("index") : 0, // Безопасное получение int
                rs.getString("constituent_entity"),
                rs.getString("area_hierarchy"),
                rs.getString("city_name"),
                rs.getString("street"),
                rs.getString("house"),
                rs.getString("coordinates"),
                rs.getString("raw_address")
        );
    }

    @Override
    public ObjectAddress findByObjectId(int objectId) {
        return findOne("object_id = ?", objectId).orElse(null);
    }
}
