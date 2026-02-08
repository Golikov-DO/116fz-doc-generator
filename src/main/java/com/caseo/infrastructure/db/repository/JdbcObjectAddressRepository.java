package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectAddress;
import com.caseo.domain.repository.ObjectAddressRepository;

import java.sql.SQLException;

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
            rs.getInt("index"),
            rs.getString("constituent_entity"),
            rs.getString("city"),
            rs.getString("street"),
            rs.getString("house")
        );
    }

    @Override
    public ObjectAddress findByObjectId(int objectId) throws SQLException {
        return findOne("object_id = ?", objectId).orElse(null);
    }
}
