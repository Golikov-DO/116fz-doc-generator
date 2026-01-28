package com.caseo.domain.repository;

import com.caseo.domain.model.HazardousSubstance;

import java.sql.SQLException;

public interface HazardousSubstanceRepository {

    HazardousSubstance findById(int id) throws SQLException;

}

