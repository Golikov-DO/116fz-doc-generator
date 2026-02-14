package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectRegionalAuthorities;
import com.caseo.domain.repository.ObjectRegionAuthoritiesRepository;

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
}