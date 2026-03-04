package com.caseo.domain.repository;

import com.caseo.domain.model.ReferenceCity;

import java.sql.SQLException;
import java.util.List;

public interface ReferenceCityRepository {

    ReferenceCity findById(int id) throws SQLException;

    List<ReferenceCity> findAll() throws SQLException;

}
