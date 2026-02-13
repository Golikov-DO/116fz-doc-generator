package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.CompositionKchs;
import com.caseo.domain.repository.CompositionKchsRepository;

import java.util.List;

public class JdbcCompositionKchsRepository extends BaseJdbcRepository<CompositionKchs> implements CompositionKchsRepository {

    @Override
    protected String table() {
        return "composition_kchs";
    }

    @Override
    protected RowMapper<CompositionKchs> mapper() {
        return rs -> new CompositionKchs(
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
    public List<CompositionKchs> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }
}