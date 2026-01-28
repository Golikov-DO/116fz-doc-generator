package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.repository.DocumentSetRepository;
import com.caseo.infrastructure.db.DbUtils;

public class JdbcDocumentSetRepository implements DocumentSetRepository {

    // --- mapper ---
    private final RowMapper<DocumentSet> mapper = rs -> {
        DocumentSet d = new DocumentSet();
        d.setId(rs.getInt("id"));
        d.setOrgId(rs.getInt("org_id"));
        return d;
    };

    @Override
    public DocumentSet findById(int id) {

        String sql = """
            SELECT id, org_id
            FROM document_set
            WHERE id = ?
        """;

        return DbUtils.queryOne(sql, mapper, id);
    }
}