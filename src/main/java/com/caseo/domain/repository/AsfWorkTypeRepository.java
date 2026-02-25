package com.caseo.domain.repository;

import com.caseo.domain.model.AsfWorkType;

import java.sql.SQLException;
import java.util.List;

public interface AsfWorkTypeRepository {

    List<AsfWorkType> findByAsfId(int asfId) throws SQLException;

    void save(AsfWorkType asfWorkType, int asfId) throws SQLException;

    void deleteByAsfId(int asfId) throws SQLException;
}
