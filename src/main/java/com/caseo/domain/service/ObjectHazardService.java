package com.caseo.domain.service;

import com.caseo.domain.model.ObjectHazardousParam;
import com.caseo.domain.model.ObjectHazardousParamValue;
import com.caseo.domain.repository.ObjectHazardousParamRepository;
import com.caseo.domain.repository.ObjectHazardousParamValueRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectHazardService {

    private final ObjectHazardousParamValueRepository objectHazardousParamValueRepository;
    private final ObjectHazardousParamRepository objectHazardousParamRepository;

    public ObjectHazardService(ObjectHazardousParamRepository objectHazardousParamRepository, ObjectHazardousParamValueRepository objectHazardousParamValueRepository) {
        this.objectHazardousParamValueRepository = objectHazardousParamValueRepository;
        this.objectHazardousParamRepository = objectHazardousParamRepository;
    }

    public List<ObjectHazardousParam> getAllParamsOrdered(int substanceId) throws SQLException {
        return objectHazardousParamRepository.findParamBySubstanceId(substanceId);
    }

    public Map<Integer, ObjectHazardousParamValue> getValuesByParam(int paramId) throws SQLException {
        return objectHazardousParamValueRepository.findByParamId(paramId)
                .stream()
                .collect(Collectors.toMap(
                        ObjectHazardousParamValue::paramId,
                        Function.identity()
                ));
    }
}

