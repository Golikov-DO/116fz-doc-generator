package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.TechnologicalBlock;
import com.caseo.domain.repository.TechnologicalBlockRepository;
import com.caseo.infrastructure.db.DbUtils;

import java.util.List;

public class JdbcTechnologicalBlockRepository extends BaseJdbcRepository<TechnologicalBlock> implements TechnologicalBlockRepository {

    @Override
    protected String table() {
        return "object_technological_block";
    }

    @Override
    protected RowMapper<TechnologicalBlock> mapper() {
        return rs -> new TechnologicalBlock(
                rs.getInt("num"),
                rs.getString("name")
        );
    }

    @Override
    public List<TechnologicalBlock> findByObjectId(int objectId) {

        return findList("object_id = ?", "ORDER BY num", objectId);

    }

    @Override
    public int countByObjectId(int objectId) {

        String sql = "SELECT COUNT(*) FROM " + table() + " WHERE object_id = ?";

        Integer count = DbUtils.queryOne(sql, rs -> rs.getInt(1), objectId);
        return count != null ? count : 0;
    }
}