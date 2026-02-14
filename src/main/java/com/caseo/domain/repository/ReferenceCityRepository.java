package com.caseo.domain.repository;

import com.caseo.domain.model.ReferenceCity;

import java.sql.SQLException;

public interface ReferenceCityRepository {

    ReferenceCity findById(int id) throws SQLException;

}
