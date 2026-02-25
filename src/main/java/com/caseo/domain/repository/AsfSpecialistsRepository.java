package com.caseo.domain.repository;

import com.caseo.domain.model.AsfSpecialists;

import java.sql.SQLException;

public interface AsfSpecialistsRepository {

    AsfSpecialists findByAsfId(int asfId) throws SQLException;

    void save(AsfSpecialists asfSpecialists, int asfId) throws SQLException;

    void deleteByAsfId(int asfId) throws SQLException;
}

