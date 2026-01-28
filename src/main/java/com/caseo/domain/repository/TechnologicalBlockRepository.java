package com.caseo.domain.repository;


import com.caseo.domain.model.TechnologicalBlock;

import java.sql.SQLException;
import java.util.List;

public interface TechnologicalBlockRepository {

    List<TechnologicalBlock> findByObjectId(int objectId) throws SQLException;

    int countByObjectId(int objectId) throws SQLException;

}
