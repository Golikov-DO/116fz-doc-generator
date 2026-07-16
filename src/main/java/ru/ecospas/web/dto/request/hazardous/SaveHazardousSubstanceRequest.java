package ru.ecospas.web.dto.request.hazardous;

import java.util.List;

public record SaveHazardousSubstanceRequest(

        String name,
        String nameGen,
        List<HazardousParamValueRequest> values

) {
}