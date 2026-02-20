package com.caseo.domain.repository;

import com.caseo.domain.model.DocumentSet;

import java.sql.SQLException;
import java.util.List;


public interface DocumentSetRepository {

    DocumentSet findById(int id) throws SQLException;

    List<DocumentSet> findAll() throws SQLException;

}