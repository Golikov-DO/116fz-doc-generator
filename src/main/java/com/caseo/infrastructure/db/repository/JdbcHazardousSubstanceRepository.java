package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.HazardousSubstance;
import com.caseo.domain.repository.HazardousSubstanceRepository;
import com.caseo.infrastructure.db.DbUtils;

public class JdbcHazardousSubstanceRepository implements HazardousSubstanceRepository {

    // --- mapper ---
    private final RowMapper<HazardousSubstance> mapper = rs -> {
        HazardousSubstance s = new HazardousSubstance();
        s.setId(rs.getInt("id"));
        s.setCode(rs.getString("code"));
        s.setName(rs.getString("name"));
        s.setName_gen(rs.getString("name_gen"));
        return s;
    };

    @Override
    public HazardousSubstance findById(int id) {

        String sql = """
            SELECT id, code, name, name_gen
            FROM hazardous_substance
            WHERE id = ?
        """;

        return DbUtils.queryOne(sql, mapper, id);
    }
}