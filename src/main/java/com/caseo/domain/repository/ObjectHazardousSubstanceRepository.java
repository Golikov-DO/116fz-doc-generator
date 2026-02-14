package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectHazardousSubstance;

import java.sql.SQLException;

public interface ObjectHazardousSubstanceRepository {

    ObjectHazardousSubstance findById(int id) throws SQLException;

}

