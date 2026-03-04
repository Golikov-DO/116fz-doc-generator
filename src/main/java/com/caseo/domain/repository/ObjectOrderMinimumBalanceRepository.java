package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectOrderMinimumBalance;

import java.sql.SQLException;

public interface ObjectOrderMinimumBalanceRepository {

    ObjectOrderMinimumBalance findByObjectId(int objectId) throws SQLException;

    void save(ObjectOrderMinimumBalance objectOrderMinimumBalance, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}
