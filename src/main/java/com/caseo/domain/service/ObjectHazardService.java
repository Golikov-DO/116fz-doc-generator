package com.caseo.domain.service;

import com.caseo.domain.model.ObjectHazardousParam;
import com.caseo.domain.model.ObjectHazardousParamValue;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectHazardService {

    private final ChildService<ObjectHazardousParam> paramService;
    private final ChildService<ObjectHazardousParamValue> valueService;

    public ObjectHazardService(
            ChildService<ObjectHazardousParam> paramService,
            ChildService<ObjectHazardousParamValue> valueService
    ) {
        this.paramService = paramService;
        this.valueService = valueService;
    }

    public List<ObjectHazardousParam> getAllParamsOrdered(int substanceId) throws SQLException {
        return paramService.getManyByParentId(substanceId);
    }

    public Map<Integer, ObjectHazardousParamValue> getValuesByParam(int paramId) throws SQLException {
        return valueService.getManyByParentId(paramId)
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getParam().getId(),
                        Function.identity()
                ));

    }
}

