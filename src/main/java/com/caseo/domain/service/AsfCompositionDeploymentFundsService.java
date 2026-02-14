package com.caseo.domain.service;

import com.caseo.domain.model.AsfCompositionDeploymentFunds;
import com.caseo.domain.model.AsfSigner;
import com.caseo.domain.repository.AsfCompositionDeploymentFundsRepository;
import com.caseo.domain.repository.AsfSignerRepository;

import java.sql.SQLException;

public class AsfCompositionDeploymentFundsService {

    private AsfCompositionDeploymentFundsRepository asfCompositionDeploymentFundsRepository;

    public AsfCompositionDeploymentFundsService(AsfCompositionDeploymentFundsRepository asfCompositionDeploymentFundsRepository) {
        this.asfCompositionDeploymentFundsRepository = asfCompositionDeploymentFundsRepository;
    }

    public AsfCompositionDeploymentFunds getByAsfId(int asfId) throws SQLException {
        return asfCompositionDeploymentFundsRepository.findByAsfId(asfId);
    }
}