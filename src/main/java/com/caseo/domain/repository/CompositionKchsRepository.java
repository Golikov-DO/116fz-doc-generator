package com.caseo.domain.repository;

import com.caseo.domain.model.CompositionKchs;

import java.sql.SQLException;
import java.util.List;

public interface CompositionKchsRepository {

    List<CompositionKchs> findByObjectId(int objectId) throws SQLException;

}