package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectCompositionKchs;
import com.caseo.domain.repository.ObjectCompositionKchsRepository;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void save(ObjectCompositionKchs objectCompositionKchs, int objectId) throws SQLException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("object_id", objectId);
        data.put("number", objectCompositionKchs.number());
        data.put("position", objectCompositionKchs.position());
        data.put("full_name", objectCompositionKchs.fullName());
        data.put("work_phone", objectCompositionKchs.workPhone());
        data.put("cell_phone", objectCompositionKchs.cellPhone());
        data.put("home_address", objectCompositionKchs.homeAddress());

        insert(data);
    }

    @Override
    public void deleteByObjectId(int objectId) throws SQLException {
        delete("object_id = ?", objectId);
    }
}