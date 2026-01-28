package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectStructureP1;

import java.sql.SQLException;
import java.util.List;

public interface ObjectStructureRepository {

    List<ObjectStructureP1> findByObjectId(int objectId) throws SQLException;

}