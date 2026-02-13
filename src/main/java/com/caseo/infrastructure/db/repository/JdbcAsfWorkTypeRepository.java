package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfWorkType;
import com.caseo.domain.repository.AsfWorkTypeRepository;

import java.util.List;

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
    public List<AsfWorkType> findByAsfId(int asfId) {
        return findList("asf_id = ?", asfId);
    }

}
