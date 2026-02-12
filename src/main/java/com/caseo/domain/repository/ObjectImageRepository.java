package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectImage;
import java.sql.SQLException;
import java.util.List;

public interface ObjectImageRepository {

    List<ObjectImage> findByObjectId(int objectId) throws SQLException;

}
