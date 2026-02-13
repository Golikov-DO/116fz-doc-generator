package com.caseo.domain.service;

import com.caseo.domain.model.EmergencyServices;
import com.caseo.domain.repository.EmergencyServicesRepository;

import java.sql.SQLException;
import java.util.List;

public class EmergencyServicesService {

    private final EmergencyServicesRepository emergencyServicesRepository;

    public EmergencyServicesService(EmergencyServicesRepository emergencyServicesRepository) {
        this.emergencyServicesRepository = emergencyServicesRepository;
    }

    public List<EmergencyServices> getAll() throws SQLException {
        return emergencyServicesRepository.findAll();
    }
}

