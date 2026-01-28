package com.caseo.domain.service;

import com.caseo.domain.model.Table1;
import com.caseo.domain.repository.Table1Repository;

import java.sql.SQLException;
import java.util.List;

public class Table1Service {

    private Table1Repository table1Repository;

    public Table1Service(Table1Repository table1Repository) {
        this.table1Repository = table1Repository;
    }

    public List<Table1> getByObject(int objectId) throws SQLException {
        return table1Repository.findByObjectId(objectId);
    }
}

