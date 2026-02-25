package com.caseo.domain.repository;

import com.caseo.domain.model.AsfSigner;

import java.sql.SQLException;
import java.util.List;

public interface AsfSignerRepository {

    AsfSigner findByAsfId(int AsfId) throws SQLException;

    List<AsfSigner> getByAsfId(int AsfId) throws SQLException;

    void save(AsfSigner asfSigner, int asfId) throws SQLException;

    void deleteByAsfId(int asfId) throws SQLException;

    AsfSigner getById(int id) throws SQLException;
}

