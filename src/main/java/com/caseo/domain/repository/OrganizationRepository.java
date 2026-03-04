package com.caseo.domain.repository;

import com.caseo.domain.model.Organization;

import java.sql.SQLException;
import java.util.List;

public interface OrganizationRepository {

    Organization findById(int id) throws SQLException;

    List<Organization> findAll() throws SQLException;

    Organization save(Organization organization) throws SQLException;

}
