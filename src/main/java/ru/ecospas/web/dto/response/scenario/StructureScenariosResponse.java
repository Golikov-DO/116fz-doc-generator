package ru.ecospas.web.dto.response.scenario;

import java.util.List;

public record StructureScenariosResponse(

        List<ScenarioListResponse> likely,
        List<ScenarioListResponse> dangerous

) {
}