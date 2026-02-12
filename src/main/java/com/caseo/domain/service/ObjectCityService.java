package com.caseo.domain.service;

import com.caseo.domain.model.ObjectCity;
import com.caseo.domain.repository.ObjectCityRepository;

import java.sql.SQLException;

public class ObjectCityService {

    private final ObjectCityRepository objectCityRepository;

    public ObjectCityService(ObjectCityRepository objectCityRepository) {
        this.objectCityRepository = objectCityRepository;
    }

    public ObjectCity getById(int id) throws SQLException {
        return objectCityRepository.findById(id);
    }
}