package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectHazardousParamValue;

import java.sql.SQLException;
import java.util.List;

public interface ObjectHazardousParamValueRepository {

    List<ObjectHazardousParamValue> findByParamId(int paramId) throws SQLException;

}

