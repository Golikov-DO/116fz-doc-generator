package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectAddress;
import com.caseo.domain.model.OrganizationAddress;

import java.sql.SQLException;

public interface OrganizationAddressRepository {

    OrganizationAddress findByOrganizationId(int organizationId) throws SQLException;

}