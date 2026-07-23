package ru.ecospas.web.mapper.scenario;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Scenario;
import ru.ecospas.web.dto.response.scenario.ScenarioListResponse;
import ru.ecospas.web.dto.response.scenario.ScenarioResponse;

import java.util.List;

@Component
public class ScenarioResponseMapper {

    public ScenarioResponse toResponse(Scenario scenario) {
        if (scenario == null) {
            return null;
        }
        return new ScenarioResponse(
                scenario.getId(),
                scenario.getName(),
                scenario.getDescription(),
                scenario.getImpactFactor()
        );
    }

    public List<ScenarioResponse> toResponses(List<Scenario> list) {
        return list.stream().map(this::toResponse).toList();
    }

    public ScenarioListResponse toListResponse(Scenario scenario) {
        if (scenario == null) {
            return null;
        }
        return new ScenarioListResponse(scenario.getId(), scenario.getName());
    }

    public List<ScenarioListResponse> toListResponses(List<Scenario> list) {
        return list.stream().map(this::toListResponse).toList();
    }
}