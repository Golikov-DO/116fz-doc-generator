package ru.ecospas.web.dto.request.scenario;

import ru.ecospas.web.dto.response.scenario.ScenarioListResponse;

import java.util.List;

public record EditStructureScenariosResponse(

        List<ScenarioListResponse> allScenarios,
        List<Integer> likely,
        List<Integer> dangerous

) {
}