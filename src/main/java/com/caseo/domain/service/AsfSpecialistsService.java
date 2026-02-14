package com.caseo.domain.service;

import com.caseo.domain.model.AsfSpecialists;
import com.caseo.domain.repository.AsfSpecialistsRepository;

import java.sql.SQLException;

public class AsfSpecialistsService {

    private AsfSpecialistsRepository asfSpecialistsRepository;

    public AsfSpecialistsService(AsfSpecialistsRepository asfSpecialistsRepository) {
        this.asfSpecialistsRepository = asfSpecialistsRepository;
    }

    public AsfSpecialists getByAsfId(int asfId) throws SQLException {
        return asfSpecialistsRepository.findByAsfId(asfId);
    }
}