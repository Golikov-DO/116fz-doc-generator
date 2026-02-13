package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectStructure;
import com.caseo.domain.repository.ObjectStructureRepository;

import java.util.List;

public class JdbcObjectStructureRepository extends BaseJdbcRepository<ObjectStructure> implements ObjectStructureRepository {

    @Override
    protected String table() {
        return "object_structure";
    }

    @Override
    protected RowMapper<ObjectStructure> mapper() {
        return rs -> new ObjectStructure(
                rs.getInt("num"),
                rs.getString("name")
        );
    }

    @Override
    public List<ObjectStructure> findByObjectId(int objectId) {
        return findList("object_id = ?", "ORDER BY num", objectId);
    }
}