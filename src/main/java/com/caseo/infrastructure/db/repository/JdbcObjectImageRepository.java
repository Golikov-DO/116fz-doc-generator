package com.caseo.infrastructure.db.repository;

import com.caseo.domain.model.ObjectImage;
import com.caseo.domain.repository.ObjectImageRepository;

import java.sql.SQLException;
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

    @Override
    public void save(ObjectImage objectImage, int objectId) throws SQLException {

    }

    @Override
    public void deleteByObjectId(int objectId) throws SQLException {
        delete("object_id = ?", objectId);
    }
}
