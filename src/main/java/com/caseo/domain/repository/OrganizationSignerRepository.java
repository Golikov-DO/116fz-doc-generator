package com.caseo.domain.repository;

import com.caseo.domain.model.OrganizationSigner;

import java.sql.SQLException;

public interface OrganizationSignerRepository {

    OrganizationSigner findByOrganizationId(int orgId) throws SQLException;

}
