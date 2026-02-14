package com.caseo.domain.repository;

import com.caseo.domain.model.AsfCompositionDeploymentFunds;

import java.sql.SQLException;

public interface AsfCompositionDeploymentFundsRepository {

    AsfCompositionDeploymentFunds findByAsfId(int asfId) throws SQLException;

}

