package com.caseo.domain.service;

import com.caseo.domain.model.AsfPersonnel;
import com.caseo.domain.model.AsfSpecialists;
import com.caseo.domain.repository.AsfSpecialistsRepository;

import java.sql.SQLException;

public class AsfSpecialistsService {

    private final AsfSpecialistsRepository asfSpecialistsRepository;

    public AsfSpecialistsService(AsfSpecialistsRepository asfSpecialistsRepository) {
        this.asfSpecialistsRepository = asfSpecialistsRepository;
    }

    public AsfSpecialists getByAsfId(int asfId) throws SQLException {
        return asfSpecialistsRepository.findByAsfId(asfId);
    }

    public void save(AsfSpecialists asfSpecialists, int asfId) throws SQLException{
        asfSpecialistsRepository.save(asfSpecialists, asfId);
    }

    public void deleteByAsfId(int asfId) throws SQLException{
        asfSpecialistsRepository.deleteByAsfId(asfId);
    }
}