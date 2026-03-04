package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectRegionalAuthorities;
import com.caseo.domain.repository.ObjectRegionAuthoritiesRepository;

import java.sql.SQLException;
import java.util.List;

public class JdbcObjectRegionAuthoritiesRepository extends BaseJdbcRepository<ObjectRegionalAuthorities> implements ObjectRegionAuthoritiesRepository {

    @Override
    protected String table() {
        return "regional_authorities";
    }

    @Override
    protected RowMapper<ObjectRegionalAuthorities> mapper() {
        return rs -> new ObjectRegionalAuthorities(
                rs.getInt("object_id"),
                rs.getString("name"),
                rs.getString("department"),
                rs.getString("phone_number"),
                rs.getString("address")
        );
    }

    @Override
    public List<ObjectRegionalAuthorities> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }

    @Override
    public void save(ObjectRegionalAuthorities objectRegionalAuthorities, int objectId) throws SQLException {

    }

    @Override
    public void deleteByObjectId(int objectId) throws SQLException {
        delete("object_id = ?", objectId);
    }
}