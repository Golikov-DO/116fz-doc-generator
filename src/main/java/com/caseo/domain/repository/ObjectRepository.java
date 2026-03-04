package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectModel;

import java.sql.SQLException;
import java.util.List;

public interface ObjectRepository {

    ObjectModel findByOrgId(int orgId) throws SQLException;

    ObjectModel findById(int id) throws SQLException;

    List<ObjectModel> findAllByOrgId(int orgId) throws SQLException;

    ObjectModel save(ObjectModel object) throws SQLException;

    void deleteById(int id) throws SQLException;

}
