package com.caseo.domain.repository;

import com.caseo.domain.model.AsfPersonnel;

import java.sql.SQLException;

public interface AsfPersonnelRepository {

    AsfPersonnel findByAsfId(int asfId) throws SQLException;

    void save(AsfPersonnel asfPersonnel, int asfId) throws SQLException;

    void deleteByAsfId(int asfId) throws SQLException;
}

