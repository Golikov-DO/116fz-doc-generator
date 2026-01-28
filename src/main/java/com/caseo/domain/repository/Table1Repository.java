package com.caseo.domain.repository;

import com.caseo.domain.model.Table1;

import java.sql.SQLException;
import java.util.List;

public interface Table1Repository {

    List<Table1> findByObjectId(int objectId) throws SQLException;

}