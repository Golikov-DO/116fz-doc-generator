package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectStructureP1;
import com.caseo.domain.repository.ObjectStructureRepository;
import com.caseo.infrastructure.db.DbUtils;

import java.util.List;

public class JdbcObjectStructureRepository implements ObjectStructureRepository {

    // --- mapper ---
    private final RowMapper<ObjectStructureP1> mapper = rs ->
            new ObjectStructureP1(
                    rs.getInt("num"),
                    rs.getString("name")
            );

    @Override
    public List<ObjectStructureP1> findByObjectId(int objectId) {

        String sql = """
            SELECT num, name
            FROM object_structure
            WHERE object_id = ?
            ORDER BY num
        """;

        return DbUtils.queryMany(sql, mapper, objectId);
    }
}