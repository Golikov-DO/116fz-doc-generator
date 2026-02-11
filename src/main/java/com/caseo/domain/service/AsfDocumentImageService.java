package com.caseo.domain.service;

import com.caseo.domain.model.AsfDocumentImage;
import com.caseo.domain.repository.AsfDocumentImageRepository;

import java.sql.SQLException;
import java.util.List;

public class AsfDocumentImageService {

    private AsfDocumentImageRepository asfDocumentImageRepository;

    public AsfDocumentImageService(AsfDocumentImageRepository asfDocumentImageRepository) {
        this.asfDocumentImageRepository = asfDocumentImageRepository;
    }

    public List<AsfDocumentImage> getByAsfId(int asfId) throws SQLException {
        return asfDocumentImageRepository.findByAsfId(asfId);
    }
}
