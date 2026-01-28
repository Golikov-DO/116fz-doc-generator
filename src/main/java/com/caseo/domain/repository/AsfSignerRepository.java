package com.caseo.domain.repository;

import com.caseo.domain.model.AsfSigner;

import java.sql.SQLException;

public interface AsfSignerRepository {

    AsfSigner findByAsfId(int asfId) throws SQLException;
}

