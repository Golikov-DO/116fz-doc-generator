package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectCompositionKchs;
import com.caseo.domain.repository.ObjectCompositionKchsRepository;

import java.util.List;

public class JdbcObjectCompositionKchsRepository extends BaseJdbcRepository<ObjectCompositionKchs> implements ObjectCompositionKchsRepository {

    @Override
    protected String table() {
        return "composition_kchs";
    }

    @Override
    protected RowMapper<ObjectCompositionKchs> mapper() {
        return rs -> new ObjectCompositionKchs(
                rs.getInt("id"),
                rs.getInt("number"),
                rs.getString("position"),
                rs.getString("full_name"),
                rs.getString("work_phone"),
                rs.getString("cell_phone"),
                rs.getString("home_address")
        );
    }

    @Override
    public List<ObjectCompositionKchs> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }
}