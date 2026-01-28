package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectAddress;
import com.caseo.domain.repository.ObjectAddressRepository;
import com.caseo.infrastructure.db.DbUtils;

import java.sql.SQLException;

public class JdbcObjectAddressRepository implements ObjectAddressRepository {

    // --- mapper ---
    private final RowMapper<ObjectAddress> mapper = rs -> {
        ObjectAddress a = new ObjectAddress();
        a.setId(rs.getInt("id"));
        a.setObjectId(rs.getLong("object_id"));
        a.setIndex(rs.getInt("index"));
        a.setConstituentEntity(rs.getString("constituent_entity"));
        a.setCity(rs.getString("city"));
        a.setStreet(rs.getString("street"));
        a.setHouse(rs.getString("house"));
        return a;
    };

    @Override
    public ObjectAddress findByObjectId(int objectId) throws SQLException {
        String sql = """
            SELECT id, object_id, "index", constituent_entity,
                   city, street, house
            FROM object_address
            WHERE object_id = ?
            LIMIT 1
        """;

        return DbUtils.queryOne(sql, mapper, objectId);
    }
}
