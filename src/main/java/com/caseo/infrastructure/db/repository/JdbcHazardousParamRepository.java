package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.HazardousParam;
import com.caseo.domain.repository.HazardousParamRepository;

import java.util.List;

public class JdbcHazardousParamRepository extends BaseJdbcRepository<HazardousParam> implements HazardousParamRepository {

    @Override
    protected String table() {
        return "hazardous_param";
    }

    protected RowMapper<HazardousParam> mapper() {
        return rs -> new HazardousParam(
                rs.getInt("id"),
                rs.getInt("substance_id"),
                rs.getString("section_no"),
                rs.getString("title"),
                rs.getString("subtitle")
        );
    }

    @Override
    public List<HazardousParam> findParamBySubstanceId(int substanceId) {
        return findList("substance_id = ?", "ORDER BY id", substanceId);
    }
}