package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectPersonsResponsible;

import java.sql.SQLException;
import java.util.List;

public interface ObjectPersonsResponsibleRepository {

    List<ObjectPersonsResponsible> findByObjectId(int objectId) throws SQLException;

}