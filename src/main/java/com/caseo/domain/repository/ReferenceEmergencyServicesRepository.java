package com.caseo.domain.repository;

import com.caseo.domain.model.ReferenceEmergencyServices;

import java.sql.SQLException;
import java.util.List;

public interface ReferenceEmergencyServicesRepository {

    List<ReferenceEmergencyServices> findAll() throws SQLException;

}