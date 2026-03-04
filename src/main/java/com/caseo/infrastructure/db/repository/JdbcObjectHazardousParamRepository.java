package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectHazardousParam;
import com.caseo.domain.repository.ObjectHazardousParamRepository;

import java.util.List;

public class JdbcObjectHazardousParamRepository extends BaseJdbcRepository<ObjectHazardousParam> implements ObjectHazardousParamRepository {

    @Override
    protected String table() {
        return "hazardous_param";
    }

    protected RowMapper<ObjectHazardousParam> mapper() {
        return rs -> new ObjectHazardousParam(
                rs.getInt("id"),
                rs.getInt("substance_id"),
                rs.getString("section_no"),
                rs.getString("title"),
                rs.getString("subtitle")
        );
    }

    @Override
    public List<ObjectHazardousParam> findParamBySubstanceId(int substanceId) {
        return findList("substance_id = ?", "ORDER BY asfId", substanceId);
    }
}