package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.HazardousParam;
import com.caseo.domain.repository.HazardousParamRepository;
import com.caseo.infrastructure.db.DbUtils;

import java.util.List;

public class JdbcHazardousParamRepository implements HazardousParamRepository {

    // --- mapper ---
    private final RowMapper<HazardousParam> mapper = rs -> {
        HazardousParam p = new HazardousParam();
        p.setId(rs.getInt("id"));
        p.setCode(rs.getString("code"));
        p.setSectionNo(rs.getString("section_no"));
        p.setTitle(rs.getString("title"));
        p.setSubtitle(rs.getString("subtitle"));
        p.setRowOrder(rs.getInt("row_order"));
        return p;
    };

    @Override
    public List<HazardousParam> findAllOrdered() {

        String sql = """
            SELECT  id, code, section_no,
                    title, subtitle, row_order
            FROM hazardous_param
            ORDER BY row_order
        """;

        return DbUtils.queryMany(sql, mapper);
    }
}