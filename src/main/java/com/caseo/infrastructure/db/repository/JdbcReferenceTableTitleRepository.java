package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ReferenceTableTitle;
import com.caseo.domain.repository.ReferenceTableTitleRepository;

import java.util.List;

public class JdbcReferenceTableTitleRepository extends BaseJdbcRepository<ReferenceTableTitle> implements ReferenceTableTitleRepository {

    @Override
    protected String table() {
        return "table_title";
    }

    @Override
    protected RowMapper<ReferenceTableTitle> mapper() {
        return rs -> new ReferenceTableTitle(
                rs.getInt("id"),
                rs.getString("table_linc"),
                rs.getString("table_name")
        );
    }

    @Override
    public List<ReferenceTableTitle> findAll() {
        // Передаем null в where, чтобы SQL был без фильтрации
        return findList(null);
    }
}
