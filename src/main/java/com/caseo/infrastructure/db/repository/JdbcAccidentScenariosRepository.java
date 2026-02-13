package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AccidentScenarios;
import com.caseo.domain.repository.AccidentScenariosRepository;

import java.util.List;

public class JdbcAccidentScenariosRepository extends BaseJdbcRepository<AccidentScenarios> implements AccidentScenariosRepository {

    @Override
    protected String table() {
        return "development_accident_scenarios";
    }

    @Override
    protected RowMapper<AccidentScenarios> mapper() {
        return rs -> new AccidentScenarios(
                rs.getInt("object_id"),
                rs.getString("scenarios"),
                rs.getString("development_scheme")
        );
    }

    @Override
    public List<AccidentScenarios> findByObjectId(int objectId) {
            return findList("object_id = ?", objectId);
    }
}
