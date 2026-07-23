package ru.ecospas.web.dto.request.hazardous;

import java.util.List;

public record SaveHazardousSubstanceRequest(

        String name,
        String nameShort,
        List<HazardousParamValueRequest> values

) {
}