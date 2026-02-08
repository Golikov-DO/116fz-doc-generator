package com.caseo.domain.service;

import com.caseo.domain.model.ObjectOrderMinimumBalance;
import com.caseo.domain.repository.ObjectOrderMinimumBalanceRepository;

import java.sql.SQLException;

public class ObjectOrderMinimumBalanceService {

    private final ObjectOrderMinimumBalanceRepository objectOrderMinimumBalanceRepository;

    public ObjectOrderMinimumBalanceService(ObjectOrderMinimumBalanceRepository objectOrderMinimumBalanceRepository) {
        this.objectOrderMinimumBalanceRepository = objectOrderMinimumBalanceRepository;
    }

    public ObjectOrderMinimumBalance getByObjectId(int id) throws SQLException {
        return objectOrderMinimumBalanceRepository.findByObjectId (id);
    }
}
