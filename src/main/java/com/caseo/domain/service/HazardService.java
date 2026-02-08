package com.caseo.domain.service;

import com.caseo.domain.model.HazardousParam;
import com.caseo.domain.model.HazardousParamValue;
import com.caseo.domain.repository.HazardousParamRepository;
import com.caseo.domain.repository.HazardousParamValueRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HazardService {

    private final HazardousParamValueRepository hazardousParamValueRepository;
    private final HazardousParamRepository hazardousParamRepository;

    public HazardService(HazardousParamRepository hazardousParamRepository, HazardousParamValueRepository hazardousParamValueRepository) {
        this.hazardousParamValueRepository = hazardousParamValueRepository;
        this.hazardousParamRepository = hazardousParamRepository;
    }

    public List<HazardousParam> getAllParamsOrdered(int substanceId) throws SQLException {
        return hazardousParamRepository.findParamBySubstanceId(substanceId);
    }

    public Map<Integer, HazardousParamValue> getValuesByParam(int paramId) throws SQLException {
        return hazardousParamValueRepository.findByParamId(paramId)
                .stream()
                .collect(Collectors.toMap(
                        HazardousParamValue::paramId,
//                        v -> v,
//                        (a, b) -> a
                        Function.identity()
                ));
    }
}

