package com.caseo.domain.repository;

import com.caseo.domain.model.AsfDocumentImage;

import java.sql.SQLException;
import java.util.List;

public interface AsfDocumentImageRepository {

        List<AsfDocumentImage> findByAsfId(int asfId) throws SQLException;

        void save(AsfDocumentImage image, int asfId) throws SQLException;

        void deleteByAsfId(int asfId) throws SQLException;
}
