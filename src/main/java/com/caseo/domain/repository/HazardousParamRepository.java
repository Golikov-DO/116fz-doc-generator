package com.caseo.domain.repository;

import com.caseo.domain.model.HazardousParam;

import java.sql.SQLException;
import java.util.List;

public interface HazardousParamRepository {

    List<HazardousParam> findParamBySubstanceId(int substanceId) throws SQLException;

}
