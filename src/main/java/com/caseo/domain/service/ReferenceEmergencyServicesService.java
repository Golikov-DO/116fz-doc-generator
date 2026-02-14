package com.caseo.domain.service;

import com.caseo.domain.model.ReferenceEmergencyServices;
import com.caseo.domain.repository.ReferenceEmergencyServicesRepository;

import java.sql.SQLException;
import java.util.List;

public class ReferenceEmergencyServicesService {

    private final ReferenceEmergencyServicesRepository referenceEmergencyServicesRepository;

    public ReferenceEmergencyServicesService(ReferenceEmergencyServicesRepository referenceEmergencyServicesRepository) {
        this.referenceEmergencyServicesRepository = referenceEmergencyServicesRepository;
    }

    public List<ReferenceEmergencyServices> getAll() throws SQLException {
        return referenceEmergencyServicesRepository.findAll();
    }
}

