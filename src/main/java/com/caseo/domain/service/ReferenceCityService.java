package com.caseo.domain.service;

import com.caseo.domain.model.ReferenceCity;
import com.caseo.domain.repository.ReferenceCityRepository;

import java.sql.SQLException;
import java.util.List;

public class ReferenceCityService {

    private final ReferenceCityRepository referenceCityRepository;

    public ReferenceCityService(ReferenceCityRepository referenceCityRepository) {
        this.referenceCityRepository = referenceCityRepository;
    }

    public ReferenceCity getById(int id) throws SQLException {
        return referenceCityRepository.findById(id);
    }

    public List<ReferenceCity> getAll() throws SQLException {
        return referenceCityRepository.findAll();
    }
}