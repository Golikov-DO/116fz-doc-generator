package com.caseo.domain.service;

import com.caseo.domain.model.ObjectInsurancePolicy;
import com.caseo.domain.repository.ObjectInsurancePolicyRepository;

import java.sql.SQLException;

public class ObjectInsurancePolicyService {

    private final ObjectInsurancePolicyRepository objectInsurancePolicyRepository;

    public ObjectInsurancePolicyService(ObjectInsurancePolicyRepository objectInsurancePolicyRepository) {
        this.objectInsurancePolicyRepository = objectInsurancePolicyRepository;
    }

    public ObjectInsurancePolicy getByObjectId(int id) throws SQLException {
        return objectInsurancePolicyRepository.findByObjectId(id);
    }
}
