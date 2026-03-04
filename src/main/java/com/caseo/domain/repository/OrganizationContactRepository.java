package com.caseo.domain.repository;

import com.caseo.domain.model.OrganizationContact;

import java.sql.SQLException;
import java.util.List;

public interface OrganizationContactRepository {

    List<OrganizationContact> findByOrganizationId(int orgId) throws SQLException;

    void save(OrganizationContact organizationContact, int orgId) throws SQLException;

    void deleteByOrganizationId(int orgId) throws SQLException;
}