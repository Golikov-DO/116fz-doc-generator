package com.caseo.domain.service;

import com.caseo.domain.model.HazardousSubstance;
import com.caseo.domain.repository.HazardousSubstanceRepository;

import java.sql.SQLException;

public class HazardousSubstanceService {

    private HazardousSubstanceRepository hazardousSubstanceRepository;

    public HazardousSubstanceService(HazardousSubstanceRepository hazardousSubstanceRepository) {
        this.hazardousSubstanceRepository = hazardousSubstanceRepository;
    }

    public HazardousSubstance getById(int id) throws SQLException {
        return hazardousSubstanceRepository.findById(id);
    }
}
