package ru.ecospas.web.dto.response.hazardous;

import java.util.List;

public record HazardousSubstanceResponse(

        Integer id,
        String name,
        String nameShort,
        List<HazardousParamValueResponse> values

) {
}