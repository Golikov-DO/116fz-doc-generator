package com.caseo.domain.repository;

import com.caseo.domain.model.EmergencyServices;

import java.sql.SQLException;
import java.util.List;

public interface EmergencyServicesRepository {

    List<EmergencyServices> findAll() throws SQLException;

}