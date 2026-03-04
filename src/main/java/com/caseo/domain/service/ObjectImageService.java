package com.caseo.domain.service;

import com.caseo.domain.model.ObjectImage;
import com.caseo.domain.repository.ObjectImageRepository;
import java.sql.SQLException;
import java.util.List;

public class ObjectImageService {

    private final ObjectImageRepository objectImageRepository;

    public ObjectImageService(ObjectImageRepository objectImageRepository) {
        this.objectImageRepository = objectImageRepository;
    }

    public List<ObjectImage> getByObjectId(int objectId) throws SQLException {
        return objectImageRepository.findByObjectId(objectId);
    }

    public void save(ObjectImage objectImage, int objectId) throws SQLException{
        objectImageRepository.save(objectImage, objectId);
    }

    public void deleteByObjectId (int objectId) throws SQLException{
        objectImageRepository.deleteByObjectId(objectId);
    }
}
