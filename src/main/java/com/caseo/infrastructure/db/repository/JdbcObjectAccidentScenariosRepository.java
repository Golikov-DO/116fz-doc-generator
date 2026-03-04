package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfWorkType;
import com.caseo.domain.model.ObjectAccidentScenarios;
import com.caseo.domain.repository.ObjectAccidentScenariosRepository;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void save(ObjectAccidentScenarios objectAccidentScenarios, int objectId) throws SQLException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("object_id", objectId);
        data.put("scenarios", objectAccidentScenarios.scenarios());
        data.put("development_scheme", objectAccidentScenarios.scheme());

        insert(data);
    }

    @Override
    public void deleteByObjectId(int objectId) throws SQLException {
        delete("object_id = ?", objectId);
    }
}
