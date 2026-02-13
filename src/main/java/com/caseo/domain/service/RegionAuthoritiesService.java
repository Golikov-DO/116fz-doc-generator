package com.caseo.domain.service;

import com.caseo.domain.model.RegionalAuthorities;
import com.caseo.domain.repository.RegionAuthoritiesRepository;

import java.sql.SQLException;
import java.util.List;

public class RegionAuthoritiesService {

    private final RegionAuthoritiesRepository regionAuthoritiesRepository;

    public RegionAuthoritiesService(RegionAuthoritiesRepository regionAuthoritiesRepository) {
        this.regionAuthoritiesRepository = regionAuthoritiesRepository;
    }

    public List<RegionalAuthorities> getByObjectId(int objectId) throws SQLException {
        return regionAuthoritiesRepository.findByObjectId(objectId);
    }
}

