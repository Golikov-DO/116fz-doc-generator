package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.TechnologicalBlock;
import com.caseo.domain.repository.TechnologicalBlockRepository;
import com.caseo.infrastructure.db.DbUtils;

import java.util.List;

public class JdbcTechnologicalBlockRepository implements TechnologicalBlockRepository {

    // --- mapper ---
    private final RowMapper<TechnologicalBlock> mapper = rs ->
            new TechnologicalBlock(
                    rs.getInt("num"),
                    rs.getString("name")
            );

    @Override
    public List<TechnologicalBlock> findByObjectId(int objectId) {

        String sql = """
            SELECT num, name
            FROM technological_block
            WHERE object_id = ?
            ORDER BY num
        """;

        return DbUtils.queryMany(sql, mapper, objectId);
    }

    @Override
    public int countByObjectId(int objectId) {

        String sql = """
            SELECT COUNT(*)
            FROM technological_block
            WHERE object_id = ?
        """;

        Integer count = DbUtils.queryOne(
                sql,
                rs -> rs.getInt(1),
                objectId
        );

        return count != null ? count : 0;
    }
}