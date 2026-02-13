package com.caseo.domain.repository;

import com.caseo.domain.model.RegionalAuthorities;

import java.sql.SQLException;
import java.util.List;

public interface RegionAuthoritiesRepository {

    List<RegionalAuthorities> findByObjectId(int objectId) throws SQLException;

}