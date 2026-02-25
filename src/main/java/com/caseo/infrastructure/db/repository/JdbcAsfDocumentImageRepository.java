package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.AsfDocumentImage;
import com.caseo.domain.repository.AsfDocumentImageRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void save(AsfDocumentImage image, int asfId) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("asf_id", asfId);
        data.put("group_key", image.groupKey());
        data.put("image_blob", image.imageBlob());
        data.put("name_document", image.nameDocument());

        insert(data);
    }

    @Override
    public void deleteByAsfId(int asfId) {
        delete("asf_id = ?", asfId);
    }
}
