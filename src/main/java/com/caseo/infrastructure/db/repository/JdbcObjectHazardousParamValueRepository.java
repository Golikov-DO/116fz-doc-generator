package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectHazardousParamValue;
import com.caseo.domain.repository.ObjectHazardousParamValueRepository;

import java.util.List;

public class JdbcObjectHazardousParamValueRepository extends BaseJdbcRepository<ObjectHazardousParamValue> implements ObjectHazardousParamValueRepository {

    @Override
    protected String table() {
        return "hazardous_param_value";
    }

    protected RowMapper<ObjectHazardousParamValue> mapper() {
        return rs -> new ObjectHazardousParamValue(
                rs.getInt("id"),
                rs.getInt("param_id"),
                rs.getString("value_text"),
                rs.getString("source_info")
        );
    }

    @Override
    public List<ObjectHazardousParamValue> findByParamId(int paramId) {
        return findList("param_id = ?", paramId);
    }
}