package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfPersonnel;
import com.caseo.domain.repository.AsfPersonnelRepository;

public class JdbcAsfPersonnelRepository extends BaseJdbcRepository<AsfPersonnel> implements AsfPersonnelRepository {

    @Override
    protected String table() {
        return "asf_personnel";
    }

    @Override
    protected RowMapper<AsfPersonnel> mapper() {
        return rs -> new AsfPersonnel(
                rs.getInt("staff_by_staffing"),
                rs.getInt("staff_by_list"),
                rs.getInt("certified_total"),
                rs.getInt("qualified_total"),
                rs.getInt("third_class"),
                rs.getInt("second_class"),
                rs.getInt("first_class"),
                rs.getInt("international_class")
        );
    }

    @Override
    public AsfPersonnel findByAsfId(int asfId) {
        return findOne("asf_id = ?", asfId).orElse(null);
    }
}
