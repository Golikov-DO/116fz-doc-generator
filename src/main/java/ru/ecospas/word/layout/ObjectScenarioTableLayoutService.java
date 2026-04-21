package ru.ecospas.word.layout;

import ru.ecospas.domain.model.ObjectStructure;
import ru.ecospas.domain.model.Scenario;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.domain.util.SubscriptUtils;
import ru.ecospas.web.dto.ScenarioDTO;

import java.util.*;
import java.util.stream.Collectors;

import static ru.ecospas.domain.util.Collect.collect;

public class ObjectScenarioTableLayoutService {

    private final ChildService<ObjectStructure> structureService;
    private final ParentService<Scenario> scenarioService;


    public ObjectScenarioTableLayoutService(
            ChildService<ObjectStructure> structureService,
            ParentService<Scenario> scenarioService
    ) {
        this.structureService = structureService;
        this.scenarioService = scenarioService;
    }

    public List<String[]> getObjectScenarioTableData(int objectId) {

        List<ObjectStructure> structures = structureService.getManyByParentId(objectId);
        List<Scenario> allScenarios = scenarioService.getMany();
        List<String[]> rows = new ArrayList<>();
        LinkedHashSet<Integer> allIds = new LinkedHashSet<>();

        for (ObjectStructure s : structures) {
            collect(s.getLikelyIds(), allIds);
            collect(s.getDangerousIds(), allIds);
        }

        Map<Integer, ScenarioDTO> map = new LinkedHashMap<>();
        int counter = 1;

        Map<Integer, Scenario> scenarioMap = allScenarios.stream()
                .collect(Collectors.toMap(Scenario::getId, s -> s));

        List<Integer> sortedIds = allIds.stream()
                .sorted()
                .toList();

        for (Integer id : sortedIds) {
            Scenario sc = scenarioMap.get(id);
            if (sc == null) continue;

            ScenarioDTO dto = new ScenarioDTO();
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
                            ScenarioDTO dto = map.get(id);
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

        List<ObjectStructure> structures = structureService.getManyByParentId(objectId);
        List<Scenario> allScenarios = scenarioService.getMany();

        LinkedHashSet<Integer> allIds = new LinkedHashSet<>();

        for (ObjectStructure s : structures) {
            collect(s.getLikelyIds(), allIds);
            collect(s.getDangerousIds(), allIds);
        }

        Map<Integer, ScenarioDTO> map = new LinkedHashMap<>();
        int counter = 1;

        Map<Integer, Scenario> scenarioMap = allScenarios.stream()
                .collect(Collectors.toMap(Scenario::getId, s -> s));

        List<Integer> sortedIds = allIds.stream()
                .sorted()
                .toList();

        for (Integer id : sortedIds) {
            Scenario sc = scenarioMap.get(id);
            if (sc == null) continue;

            ScenarioDTO dto = new ScenarioDTO();
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

        for (ScenarioDTO dto : map.values()) {

            rows.add(new String[]{
                    "С" + SubscriptUtils.toSubscript(dto.getNumber()) + "\n" + dto.getName(),
                    dto.getDescription(),
                    dto.getImpactFactor()
            });
        }

        return rows;
    }
}