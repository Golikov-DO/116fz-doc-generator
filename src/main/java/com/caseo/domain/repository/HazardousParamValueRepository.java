package com.caseo.domain.repository;

import com.caseo.domain.model.HazardousParamValue;

import java.sql.SQLException;
import java.util.List;

public interface HazardousParamValueRepository {

    List<HazardousParamValue> findByParamId(int paramId) throws SQLException;

}

