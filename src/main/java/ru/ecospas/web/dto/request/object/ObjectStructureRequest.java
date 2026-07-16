package ru.ecospas.web.dto.request.object;

import java.util.List;

public record ObjectStructureRequest(

        Integer id,
        Integer num,
        String name,

        List<ObjectScenarioRequest> scenarios

) {
}