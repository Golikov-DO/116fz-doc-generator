package com.caseo.domain.repository;

import com.caseo.domain.model.PersonsResponsible;

import java.sql.SQLException;
import java.util.List;

public interface PersonsResponsibleRepository {

    List<PersonsResponsible> findByObjectId(int objectId) throws SQLException;

}