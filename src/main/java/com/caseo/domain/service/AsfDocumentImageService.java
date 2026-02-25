package com.caseo.domain.service;

import com.caseo.domain.model.AsfDocumentImage;
import com.caseo.domain.repository.AsfDocumentImageRepository;

import java.sql.SQLException;
import java.util.List;

public class AsfDocumentImageService {

    private final AsfDocumentImageRepository asfDocumentImageRepository;

    public AsfDocumentImageService(AsfDocumentImageRepository asfDocumentImageRepository) {
        this.asfDocumentImageRepository = asfDocumentImageRepository;
    }

    public List<AsfDocumentImage> getByAsfId(int asfId) throws SQLException {
        return asfDocumentImageRepository.findByAsfId(asfId);
    }

    public void save(AsfDocumentImage image, int asfId) throws SQLException{
        asfDocumentImageRepository.save(image, asfId);
    }

    public void deleteByAsfId(int asfId) throws SQLException{
        asfDocumentImageRepository.deleteByAsfId(asfId);
    }
}
