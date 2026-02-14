package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectAccidentScenarios;
import com.caseo.domain.repository.ObjectAccidentScenariosRepository;

import java.util.List;

public class JdbcObjectAccidentScenariosRepository extends BaseJdbcRepository<ObjectAccidentScenarios> implements ObjectAccidentScenariosRepository {

    @Override
    protected String table() {
        return "development_accident_scenarios";
    }

    @Override
    protected RowMapper<ObjectAccidentScenarios> mapper() {
        return rs -> new ObjectAccidentScenarios(
                rs.getString("scenarios"),
                rs.getString("development_scheme")
        );
    }

    @Override
    public List<ObjectAccidentScenarios> findByObjectId(int objectId) {
            return findList("object_id = ?", objectId);
    }
}
