package ru.ecospas.web.dto.response.object;

import java.util.List;

public record ObjectStructureResponse(

        Integer id,
        Integer num,
        String name,

        List<ObjectScenarioResponse> scenarios

) {
}