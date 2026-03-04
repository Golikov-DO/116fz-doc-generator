package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectInsurancePolicy;

import java.sql.SQLException;

public interface ObjectInsurancePolicyRepository {

    ObjectInsurancePolicy findByObjectId(int objectId) throws SQLException;

    void save(ObjectInsurancePolicy objectInsurancePolicy, int objectId) throws SQLException;

    void deleteByObjectId(int objectId) throws SQLException;
}
