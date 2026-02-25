package com.caseo.domain.service;

import com.caseo.domain.model.AsfCompositionDeploymentFunds;
import com.caseo.domain.repository.AsfCompositionDeploymentFundsRepository;

import java.sql.SQLException;

public class AsfCompositionDeploymentFundsService {

    private final AsfCompositionDeploymentFundsRepository asfCompositionDeploymentFundsRepository;

    public AsfCompositionDeploymentFundsService(AsfCompositionDeploymentFundsRepository asfCompositionDeploymentFundsRepository) {
        this.asfCompositionDeploymentFundsRepository = asfCompositionDeploymentFundsRepository;
    }

    public AsfCompositionDeploymentFunds getByAsfId(int asfId) throws SQLException {
        return asfCompositionDeploymentFundsRepository.findByAsfId(asfId);
    }

    public void save(AsfCompositionDeploymentFunds asfCompositionDeploymentFunds, int asfId) throws SQLException {
        asfCompositionDeploymentFundsRepository.save(asfCompositionDeploymentFunds, asfId);
    }

    public void deleteByAsfId(int asfId) throws SQLException{
        asfCompositionDeploymentFundsRepository.deleteByAsfId(asfId);
    }
}