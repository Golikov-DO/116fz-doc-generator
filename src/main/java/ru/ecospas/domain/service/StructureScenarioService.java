package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.ObjectScenarioRepository;
import ru.ecospas.domain.repository.ObjectStructureRepository;
import ru.ecospas.domain.repository.ScenarioRepository;
import ru.ecospas.web.dto.request.scenario.AssignStructureScenariosRequest;
import ru.ecospas.web.dto.request.scenario.EditStructureScenariosResponse;
import ru.ecospas.web.dto.response.scenario.ScenarioListResponse;
import ru.ecospas.web.dto.response.scenario.StructureScenariosResponse;
import ru.ecospas.web.mapper.scenario.ScenarioResponseMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StructureScenarioService {

    private final ObjectScenarioRepository objectScenarioRepository;
    private final ObjectStructureRepository structureRepository;
    private final ScenarioRepository scenarioRepository;

    private final ScenarioService scenarioService;

    private final ScenarioResponseMapper responseMapper;

    private ObjectScenario create(
            ObjectStructure structure,
            Scenario scenario,
            ScenarioType type
    ) {
        ObjectScenario objectScenario = new ObjectScenario();
        objectScenario.setStructure(structure);
        objectScenario.setScenario(scenario);
        objectScenario.setType(type);
        return objectScenario;
    }

    @Transactional
    public void save(Integer structureId, AssignStructureScenariosRequest request) {
        ObjectStructure structure = structureRepository.findById(structureId) .orElseThrow();
        objectScenarioRepository.deleteByStructureId(structureId);
        List<ObjectScenario> list = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        if (request.likely() != null) {
            ids.addAll(request.likely());
        }
        if (request.dangerous() != null) {
            ids.addAll(request.dangerous());
        }
        Map<Integer, Scenario> scenarioMap =
                scenarioRepository.findByIdIn(ids)
                        .stream()
                        .collect(Collectors.toMap(
                                Scenario::getId,
                                Function.identity()
                        ));
        if (request.likely() != null) {
            for (Integer id : request.likely()) {
                Scenario scenario = scenarioMap.get(id);
                if (scenario == null) {
                    continue;
                }
                list.add(create(structure, scenario, ScenarioType.LIKELY));
            }
        }

        if (request.dangerous() != null) {
            for (Integer id : request.dangerous()) {
                Scenario scenario = scenarioMap.get(id);
                if (scenario == null) {
                    continue;
                }
                list.add(create(structure, scenario, ScenarioType.DANGEROUS));
            }
        }
        objectScenarioRepository.saveAll(list);
    }

    @Transactional(readOnly = true)
    public EditStructureScenariosResponse edit(Integer structureId) {
        List<ScenarioListResponse> allScenarios =
                responseMapper.toListResponses(scenarioService.findAll());
        List<Integer> likely = new ArrayList<>();
        List<Integer> dangerous = new ArrayList<>();
        List<ObjectScenario> objectScenarios = objectScenarioRepository
                        .findByStructureIdOrderByTypeAscScenarioNameAsc(structureId);
        for (ObjectScenario objectScenario : objectScenarios) {
            if (objectScenario.getType() == ScenarioType.LIKELY) {
                likely.add(objectScenario.getScenario().getId());
            } else {
                dangerous.add(objectScenario.getScenario().getId());
            }
        }
        return new EditStructureScenariosResponse(allScenarios, likely, dangerous);
    }

    @Transactional(readOnly = true)
    public StructureScenariosResponse load(Integer structureId) {
        List<ObjectScenario> objectScenarios = objectScenarioRepository
                        .findByStructureIdOrderByTypeAscScenarioNameAsc(structureId);
        List<ScenarioListResponse> likely = new ArrayList<>();
        List<ScenarioListResponse> dangerous = new ArrayList<>();
        for (ObjectScenario objectScenario : objectScenarios) {
            Scenario scenario = objectScenario.getScenario();
            ScenarioListResponse dto = responseMapper.toListResponse(scenario);
            if (objectScenario.getType() == ScenarioType.LIKELY) {
                likely.add(dto);
            } else {
                dangerous.add(dto);
            }
        }
        return new StructureScenariosResponse(likely, dangerous);
    }
}