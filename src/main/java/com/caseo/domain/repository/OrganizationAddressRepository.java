package com.caseo.domain.repository;

import com.caseo.domain.model.OrganizationAddress;

import java.sql.SQLException;

public interface OrganizationAddressRepository {

    OrganizationAddress findByOrganizationId(int orgId) throws SQLException;

    void save(OrganizationAddress organizationAddress, int orgId) throws SQLException;

    void deleteByOrganizationId(int orgId) throws SQLException;
}