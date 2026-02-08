package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.repository.DocumentSetRepository;

public class JdbcDocumentSetRepository extends BaseJdbcRepository<DocumentSet> implements DocumentSetRepository {

    @Override
    protected String table() {
        return "document_set";
    }

    @Override
    protected RowMapper<DocumentSet> mapper() {
        return rs -> new DocumentSet(
        rs.getInt("id"),
        rs.getInt("org_id")
        );
    }

    @Override
    public DocumentSet findById(int id){
        return findOne("org_id = ?", id).orElse(null);
    }
}