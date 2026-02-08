package com.caseo.domain.repository;

import com.caseo.domain.model.AsfWorkType;

import java.sql.SQLException;

public interface AsfWorkTypeRepository {

    AsfWorkType findByAsfId(int asfId) throws SQLException;

}
