package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfWorkType;
import com.caseo.domain.repository.AsfWorkTypeRepository;

public class JdbcAsfWorkTypeRepository extends BaseJdbcRepository<AsfWorkType> implements AsfWorkTypeRepository {

    @Override
    protected String table() {
        return "asf_work_type";
    }

    @Override
    protected RowMapper<AsfWorkType> mapper() {
        return rs -> new AsfWorkType(
                rs.getInt("id"),
                rs.getString("name")
        );
    }

    @Override
    public AsfWorkType findByAsfId(int asfId) {
        return findOne("asf_id = ?", asfId).orElse(null);
    }
}
