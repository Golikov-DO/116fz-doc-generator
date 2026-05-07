package ru.ecospas.domain.service;

import ru.ecospas.domain.model.Scenario;
import ru.ecospas.web.dto.ScenarioDTO;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScenarioNumberService {

    public Map<Integer, ScenarioDTO> build(List<Scenario> scenarios) {

        Map<Integer, ScenarioDTO> map = new LinkedHashMap<>();
        int counter = 1;

        for (Scenario sc : scenarios) {
            ScenarioDTO dto = new ScenarioDTO();
            dto.setId(sc.getId());
            dto.setName(sc.getName());
            dto.setNumber(counter++);

            map.put(sc.getId(), dto);
        }

        return map;
    }

    public Map<Integer, ScenarioDTO> buildFromIds(
            Collection<Integer> ids,
            Map<Integer, Scenario> scenarioMap
    ) {

        Map<Integer, ScenarioDTO> map = new LinkedHashMap<>();
        int counter = 1;

        for (Integer id : ids) {
            Scenario sc = scenarioMap.get(id);
            if (sc == null) continue;

            ScenarioDTO dto = new ScenarioDTO();
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