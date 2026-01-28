package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.HazardousParamValue;
import com.caseo.domain.repository.HazardousParamValueRepository;
import com.caseo.infrastructure.db.DbUtils;

import java.util.List;

public class JdbcHazardousParamValueRepository implements HazardousParamValueRepository {

    // --- mapper ---
    private final RowMapper<HazardousParamValue> mapper = rs -> {
        HazardousParamValue v = new HazardousParamValue();
        v.setId(rs.getInt("id"));
        v.setSubstanceId(rs.getInt("substance_id"));
        v.setParamId(rs.getInt("param_id"));
        v.setValueText(rs.getString("value_text"));
        v.setSourceInfo(rs.getString("source_info"));
        return v;
    };

    @Override
    public List<HazardousParamValue> findBySubstanceId(int substanceId) {

        String sql = """
            SELECT  id, substance_id, param_id,
                    value_text, source_info
            FROM hazardous_param_value
            WHERE substance_id = ?
        """;

        return DbUtils.queryMany(sql, mapper, substanceId);
    }
}