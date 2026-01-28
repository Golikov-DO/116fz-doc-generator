package com.caseo.domain.repository;

import com.caseo.domain.model.OrgSigner;

import java.sql.SQLException;

public interface OrgSignerRepository {

    OrgSigner findByOrganizationId(int orgId) throws SQLException;

}
