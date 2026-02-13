package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.MainScenarios;
import com.caseo.domain.repository.MainScenariosRepository;

import java.util.List;

public class JdbcMainScenariosRepository extends BaseJdbcRepository<MainScenarios> implements MainScenariosRepository {

    @Override
    protected String table() {
        return "main_scenarios";
    }

    @Override
    protected RowMapper<MainScenarios> mapper() {
        return rs -> new MainScenarios(
                rs.getInt("object_id"),
                rs.getString("name_equipment"),
                rs.getString("event"),
                rs.getString("list_scenarios")
        );
    }

    @Override
    public List<MainScenarios> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }
}