package ru.ecospas.domain.service;

import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Scenario;
import ru.ecospas.web.dto.response.scenario.WordScenarioResponse;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScenarioNumberService {

    public Map<Integer, WordScenarioResponse> build(List<Scenario> scenarios) {

        Map<Integer, WordScenarioResponse> map = new LinkedHashMap<>();
        int counter = 1;

        for (Scenario sc : scenarios) {
            WordScenarioResponse dto = new WordScenarioResponse();
            dto.setId(sc.getId());
            dto.setName(sc.getName());
            dto.setNumber(counter++);

            map.put(sc.getId(), dto);
        }

        return map;
    }

    public Map<Integer, WordScenarioResponse> buildFromIds(
            Collection<Integer> ids,
            Map<Integer, Scenario> scenarioMap
    ) {

        Map<Integer, WordScenarioResponse> map = new LinkedHashMap<>();
        int counter = 1;

        for (Integer id : ids) {
            Scenario sc = scenarioMap.get(id);
            if (sc == null) continue;

            WordScenarioResponse dto = new WordScenarioResponse();
            dto.setId(id);
            dto.setName(sc.getName());
            dto.setDescription(sc.getDescription());
            dto.setImpactFactor(sc.getImpactFactor());
            dto.setNumber(counter++);

            map.put(id, dto);
        }

        return map;
    }
}