package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfDocumentImage;
import com.caseo.domain.repository.AsfDocumentImageRepository;

import java.util.List;

public class JdbcAsfDocumentImageRepository extends BaseJdbcRepository<AsfDocumentImage> implements AsfDocumentImageRepository {

    @Override
    protected String table() {
        return "asf_document_image";
    }

    protected RowMapper<AsfDocumentImage> mapper() {
        return rs -> new AsfDocumentImage(
                rs.getString("group_key"),
                rs.getBytes("image_blob"),
                rs.getString("name_document")
        );
    }

    @Override
    public List<AsfDocumentImage> findByAsfId(int asfId) {
        // Сортируем сначала по группе, потом по порядку внутри группы
        return findList("asf_id = ?", "ORDER BY group_key, id", asfId);
    }
}
