package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectInsurancePolicy;

import java.sql.SQLException;

public interface ObjectInsurancePolicyRepository {

    ObjectInsurancePolicy findByObjectId(int objectId) throws SQLException;
}
