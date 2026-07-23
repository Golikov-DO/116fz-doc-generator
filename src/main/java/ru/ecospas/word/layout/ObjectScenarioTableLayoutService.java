package ru.ecospas.word.layout;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ObjectStructure;
import ru.ecospas.domain.model.Scenario;
import ru.ecospas.domain.repository.ObjectStructureRepository;
import ru.ecospas.domain.repository.ScenarioRepository;
import ru.ecospas.domain.util.SubscriptUtils;
import ru.ecospas.web.dto.response.scenario.WordScenarioResponse;

import java.util.*;
import java.util.stream.Collectors;

import static ru.ecospas.domain.util.Collect.collect;

@Component
@RequiredArgsConstructor
public class ObjectScenarioTableLayoutService {

    private final ObjectStructureRepository structureRepository;
    private final ScenarioRepository scenarioRepository;

    public List<String[]> getObjectScenarioTableData(int objectId) {

        List<ObjectStructure> structures = structureRepository.findAllByObjectId(objectId);
        List<Scenario> allScenarios = scenarioRepository.findAll();
        List<String[]> rows = new ArrayList<>();
        LinkedHashSet<Integer> allIds = new LinkedHashSet<>();

        for (ObjectStructure s : structures) {
            collect(s.getLikelyIds(), allIds);
            collect(s.getDangerousIds(), allIds);
        }

        Map<Integer, WordScenarioResponse> map = new LinkedHashMap<>();
        int counter = 1;

        Map<Integer, Scenario> scenarioMap = allScenarios.stream()
                .collect(Collectors.toMap(Scenario::getId, s -> s));

        List<Integer> sortedIds = allIds.stream()
                .sorted()
                .toList();

        for (Integer id : sortedIds) {
            Scenario sc = scenarioMap.get(id);
            if (sc == null) continue;

            WordScenarioResponse dto = new WordScenarioResponse();
            dto.setId(id);
            dto.setName(sc.getName());
            dto.setDescription(sc.getDescription());
            dto.setNumber(counter++);

            map.put(id, dto);
        }

        for (ObjectStructure structure : structures) {

            LinkedHashSet<Integer> ids = new LinkedHashSet<>();

            collect(structure.getLikelyIds(), ids);
            collect(structure.getDangerousIds(), ids);

            if (!ids.isEmpty()) {

                String joined = ids.stream()
                        .sorted(Comparator.comparing(id -> map.get(id).getNumber()))
                        .map(id -> {
                            WordScenarioResponse dto = map.get(id);
                            return dto != null ? "С" + SubscriptUtils.toSubscript(dto.getNumber()) : "";
                        })
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining(", "));

                rows.add(new String[]{
                        structure.getName(),
                        joined
                });
            }
        }

        return rows;
    }

    public List<String[]> getObjectScenarioFullData(int objectId) {

        List<ObjectStructure> structures = structureRepository.findAllByObjectId(objectId);
        List<Scenario> allScenarios = scenarioRepository.findAll();

        LinkedHashSet<Integer> allIds = new LinkedHashSet<>();

        for (ObjectStructure s : structures) {
            collect(s.getLikelyIds(), allIds);
            collect(s.getDangerousIds(), allIds);
        }

        Map<Integer, WordScenarioResponse> map = new LinkedHashMap<>();
        int counter = 1;

        Map<Integer, Scenario> scenarioMap = allScenarios.stream()
                .collect(Collectors.toMap(Scenario::getId, s -> s));

        List<Integer> sortedIds = allIds.stream()
                .sorted()
                .toList();

        for (Integer id : sortedIds) {
            Scenario sc = scenarioMap.get(id);
            if (sc == null) continue;

            WordScenarioResponse dto = new WordScenarioResponse();
            dto.setId(id);
            dto.setName(sc.getName());
            dto.setDescription(sc.getDescription());
            dto.setImpactFactor(sc.getImpactFactor());
            dto.setNumber(counter);
            dto.setDisplayName("С" + SubscriptUtils.toSubscript(counter) + "\n" + sc.getName());

            counter++;

            map.put(id, dto);
        }

        List<String[]> rows = new ArrayList<>();

        for (WordScenarioResponse dto : map.values()) {

            rows.add(new String[]{
                    "С" + SubscriptUtils.toSubscript(dto.getNumber()) + "\n" + dto.getName(),
                    dto.getDescription(),
                    dto.getImpactFactor()
            });
        }

        return rows;
    }
}