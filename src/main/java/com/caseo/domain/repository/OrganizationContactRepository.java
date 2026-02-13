package com.caseo.domain.repository;

import com.caseo.domain.model.OrganizationContact;

import java.sql.SQLException;
import java.util.List;

public interface OrganizationContactRepository {

    List<OrganizationContact> findByOrganizationId(int organizationId) throws SQLException;

}