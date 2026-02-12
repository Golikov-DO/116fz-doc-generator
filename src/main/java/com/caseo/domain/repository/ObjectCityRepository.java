package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectCity;

import java.sql.SQLException;

public interface ObjectCityRepository {

    ObjectCity findById(int id) throws SQLException;

}
