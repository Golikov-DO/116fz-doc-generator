package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfWorkType;
import com.caseo.domain.repository.AsfWorkTypeRepository;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void save(AsfWorkType asfWorkType, int asfId) throws SQLException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("asf_id", asfId);
        data.put("name", asfWorkType.name());

        insert(data);
    }

    @Override
    public void deleteByAsfId(int asfId) throws SQLException {
        delete("asf_id = ?", asfId);
    }
}
