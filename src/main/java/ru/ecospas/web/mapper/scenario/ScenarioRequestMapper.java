package ru.ecospas.web.mapper.scenario;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Scenario;
import ru.ecospas.web.dto.request.scenario.SaveScenarioRequest;

@Component
public class ScenarioRequestMapper {

    public void toScenario(SaveScenarioRequest request, Scenario scenario) {
        scenario.setName(request.name());
        scenario.setDescription(request.description());
        scenario.setImpactFactor(request.impactFactor());
    }
}