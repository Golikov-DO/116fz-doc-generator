package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectImage;
import java.sql.SQLException;
import java.util.List;

public interface ObjectImageRepository {

    List<ObjectImage> findByObjectId(int objectId) throws SQLException;

    void save(ObjectImage objectImage, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}
