package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectTableTitle;
import com.caseo.domain.repository.ObjectTableTitleRepository;

import java.util.List;

public class JdbcObjectTableTitleRepository extends BaseJdbcRepository<ObjectTableTitle> implements ObjectTableTitleRepository {

    @Override
    protected String table() {
        return "table_title";
    }

    @Override
    protected RowMapper<ObjectTableTitle> mapper() {
        return rs -> new ObjectTableTitle(
                rs.getInt("id"),
                rs.getString("table_linc"),
                rs.getString("table_name")
        );
    }

    @Override
    public List<ObjectTableTitle> findAll() {
        // Передаем null в where, чтобы SQL был без фильтрации
        return findList(null);
    }
}
