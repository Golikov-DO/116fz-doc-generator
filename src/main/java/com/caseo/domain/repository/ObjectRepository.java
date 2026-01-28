package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectModel;

import java.sql.SQLException;

public interface ObjectRepository {

    ObjectModel findByOrgId(int orgId) throws SQLException;

}
