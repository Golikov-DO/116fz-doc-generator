package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.repository.DocumentSetRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JdbcDocumentSetRepository extends BaseJdbcRepository<DocumentSet> implements DocumentSetRepository {

    @Override
    protected String table() {
        return "document_set";
    }

    @Override
    protected RowMapper<DocumentSet> mapper() {
        return rs -> new DocumentSet(
                rs.getInt("id"),
                rs.getInt("org_id"),
                rs.getInt("object_id")
        );
    }

    @Override
    public DocumentSet findById(int id) {
        return findOne("id = ?", id).orElse(null);
    }

    @Override
    public List<DocumentSet> findAll() {
        return findList(null);
    }

    @Override
    public DocumentSet save(DocumentSet documentSet) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("org_id", documentSet.orgId());
        data.put("object_id", documentSet.objectId());

        int newId = insert(data);

        return new DocumentSet(newId, documentSet.orgId(), documentSet.objectId());
    }
}