package com.caseo.domain.repository;


import com.caseo.domain.model.Organization;

import java.sql.SQLException;

public interface OrganizationRepository {

    Organization findById(int id) throws SQLException;

}
