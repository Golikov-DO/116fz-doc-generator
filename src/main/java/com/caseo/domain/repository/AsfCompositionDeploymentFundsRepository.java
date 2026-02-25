package com.caseo.domain.repository;

import com.caseo.domain.model.AsfCompositionDeploymentFunds;

import java.sql.SQLException;

public interface AsfCompositionDeploymentFundsRepository {

    AsfCompositionDeploymentFunds findByAsfId(int asfId) throws SQLException;

    void save(AsfCompositionDeploymentFunds asfCompositionDeploymentFunds, int asfId) throws SQLException;

    void deleteByAsfId(int asfId) throws SQLException;
}

