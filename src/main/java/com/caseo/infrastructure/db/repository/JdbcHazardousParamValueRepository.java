package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.HazardousParamValue;
import com.caseo.domain.repository.HazardousParamValueRepository;

import java.util.List;

public class JdbcHazardousParamValueRepository extends BaseJdbcRepository<HazardousParamValue> implements HazardousParamValueRepository {

    @Override
    protected String table() {
        return "hazardous_param_value";
    }

    protected RowMapper<HazardousParamValue> mapper() {
        return rs -> new HazardousParamValue(
                rs.getInt("id"),
                rs.getInt("param_id"),
                rs.getString("value_text"),
                rs.getString("source_info")
        );
    }

    @Override
    public List<HazardousParamValue> findByParamId(int paramId) {
        return findList("param_id = ?", paramId);
    }
}