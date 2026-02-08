package com.caseo.domain.service;

import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.repository.ObjectRepository;

import java.sql.SQLException;

public class ObjectService {

    private final ObjectRepository objectRepository;

    public ObjectService(ObjectRepository objectRepository) {
        this.objectRepository = objectRepository;
    }

    public ObjectModel getByOrgId(int orgId) throws SQLException {
        return objectRepository.findByOrgId(orgId);
    }
}
