package com.caseo.domain.service;

import com.caseo.domain.model.HazardousParam;
import com.caseo.domain.model.HazardousParamValue;
import com.caseo.domain.repository.HazardousParamRepository;
import com.caseo.domain.repository.HazardousParamValueRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HazardService {

    private HazardousParamValueRepository hazardousParamValueRepository;
    private HazardousParamRepository hazardousParamRepository;

    public HazardService(HazardousParamRepository hazardousParamRepository, HazardousParamValueRepository hazardousParamValueRepository) {
        this.hazardousParamValueRepository = hazardousParamValueRepository;
        this.hazardousParamRepository = hazardousParamRepository;
    }

    public List<HazardousParam> getAllParamsOrdered() throws SQLException {
        return hazardousParamRepository.findAllOrdered();
    }

    public Map<Integer, HazardousParamValue> getValuesBySubstance(int substanceId) throws SQLException {
        return hazardousParamValueRepository.findBySubstanceId(substanceId)
                .stream()
                .collect(Collectors.toMap(
                        HazardousParamValue::getParamId,
                        v -> v,
                        (a, b) -> a
                ));
    }
}

