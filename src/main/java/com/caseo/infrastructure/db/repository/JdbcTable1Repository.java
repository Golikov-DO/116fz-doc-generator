package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.Table1;
import com.caseo.domain.repository.Table1Repository;
import com.caseo.infrastructure.db.DbUtils;

import java.util.List;

public class JdbcTable1Repository implements Table1Repository {

    // --- mapper ---
    private final RowMapper<Table1> mapper = rs ->
            new Table1(
                    rs.getInt("id"),
                    rs.getInt("num"),
                    rs.getString("equipment_name"),
                    rs.getString("characteristics"),
                    rs.getInt("object_id")   // берём из БД, а не из параметра
            );

    @Override
    public List<Table1> findByObjectId(int objectId) {

        String sql = """
            SELECT id, num, equipment_name, characteristics, object_id
            FROM table_1
            WHERE object_id = ?
            ORDER BY num
        """;

        return DbUtils.queryMany(sql, mapper, objectId);
    }
}