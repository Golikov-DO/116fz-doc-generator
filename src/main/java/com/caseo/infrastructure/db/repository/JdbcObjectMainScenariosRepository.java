package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectMainScenarios;
import com.caseo.domain.repository.ObjectMainScenariosRepository;

import java.sql.SQLException;
import java.util.List;

public class JdbcObjectMainScenariosRepository extends BaseJdbcRepository<ObjectMainScenarios> implements ObjectMainScenariosRepository {

    @Override
    protected String table() {
        return "main_scenarios";
    }

    @Override
    protected RowMapper<ObjectMainScenarios> mapper() {
        return rs -> new ObjectMainScenarios(
                rs.getInt("object_id"),
                rs.getString("name_equipment"),
                rs.getString("event"),
                rs.getString("list_scenarios")
        );
    }

    @Override
    public List<ObjectMainScenarios> findByObjectId(int objectId) {
        return findList("object_id = ?", objectId);
    }

    @Override
    public void save(ObjectMainScenarios objectMainScenarios, int objectId) throws SQLException {

    }

    @Override
    public void deleteByObjectId(int objectId) throws SQLException {
        delete("object_id = ?", objectId);
    }
}