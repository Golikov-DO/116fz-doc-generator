package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.RegionalAuthorities;
import com.caseo.domain.repository.RegionAuthoritiesRepository;

import java.util.List;

public class JdbcRegionAuthoritiesRepository extends BaseJdbcRepository<RegionalAuthorities> implements RegionAuthoritiesRepository {

    @Override
    protected String table() {
        return "regional_authorities";
    }

    @Override
    protected RowMapper<RegionalAuthorities> mapper() {
        return rs -> new RegionalAuthorities(
                rs.getInt("object_id"),
                rs.getString("name"),
                rs.getString("department"),
                rs.getString("phone_number"),
                rs.getString("address")
        );
    }

    @Override
    public List<RegionalAuthorities> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }
}