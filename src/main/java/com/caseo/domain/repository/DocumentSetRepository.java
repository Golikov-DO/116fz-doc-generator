package com.caseo.domain.repository;

import com.caseo.domain.model.DocumentSet;

import java.sql.SQLException;


public interface DocumentSetRepository {

    DocumentSet findById(int id) throws SQLException;

}