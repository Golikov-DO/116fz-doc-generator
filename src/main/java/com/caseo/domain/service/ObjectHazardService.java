package com.caseo.domain.service;

import com.caseo.domain.model.ReferenceHazardousParam;
import com.caseo.domain.model.ObjectHazardousParamValue;
import com.caseo.web.dto.HazardParamDto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectHazardService {

    private final ParentService<ReferenceHazardousParam> paramService;
    private final ChildService<ObjectHazardousParamValue> valueService;

    public ObjectHazardService(
            ParentService<ReferenceHazardousParam> paramService,
            ChildService<ObjectHazardousParamValue> valueService
    ) {
        this.paramService = paramService;
        this.valueService = valueService;
    }

    public List<HazardParamDto> getHazardParamsWithValues(int substanceId) {

        List<ReferenceHazardousParam> params = paramService.getMany();

        Map<Integer, ObjectHazardousParamValue> values =
                valueService.getManyByParentId(substanceId)
                        .stream()
                        .collect(Collectors.toMap(
                                value -> value.getParam().getId(),
                                Function.identity()
                        ));

        return params.stream()
                .map(param -> new HazardParamDto(
                        param,
                        values.get(param.getId())
                ))
                .toList();
    }
}

