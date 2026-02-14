package com.caseo.domain.repository;

import com.caseo.domain.model.ObjectHazardousParam;

import java.sql.SQLException;
import java.util.List;

public interface ObjectHazardousParamRepository {

    List<ObjectHazardousParam> findParamBySubstanceId(int substanceId) throws SQLException;

}
