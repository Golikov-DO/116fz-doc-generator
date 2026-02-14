package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectImage;
import com.caseo.domain.repository.ObjectImageRepository;

import java.util.List;

public class JdbcObjectImageRepository extends BaseJdbcRepository<ObjectImage> implements ObjectImageRepository {

    @Override
    protected String table() {
        return "object_image";
    }

    protected RowMapper<ObjectImage> mapper() {
        return rs -> new ObjectImage(
                rs.getString("group_key"),
                rs.getBytes("image_blob"),
                rs.getString("caption"),
                rs.getString("link_text")
        );
    }

    @Override
    public List<ObjectImage> findByObjectId(int objectId) {
        // Сортируем сначала по группе, потом по порядку внутри группы
        return findList("object_id = ?", "ORDER BY group_key, id", objectId);
    }
}
