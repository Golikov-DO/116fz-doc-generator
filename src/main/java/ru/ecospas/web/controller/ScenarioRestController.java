package ru.ecospas.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.model.Scenario;
import ru.ecospas.domain.service.ScenarioService;
import ru.ecospas.web.dto.request.scenario.SaveScenarioRequest;
import ru.ecospas.web.dto.response.scenario.ScenarioListResponse;
import ru.ecospas.web.dto.response.scenario.ScenarioResponse;
import ru.ecospas.web.mapper.scenario.ScenarioResponseMapper;

import java.util.List;

@RestController
@RequestMapping("/api/scenarios")
@RequiredArgsConstructor
public class ScenarioRestController {

    private final ScenarioService scenarioService;
    private final ScenarioResponseMapper responseMapper;

    @GetMapping
    public List<ScenarioListResponse> getScenarios() {
        return responseMapper.toListResponses(scenarioService.findAll());
    }

    @GetMapping("/{id}")
    public ScenarioResponse getScenario(
            @PathVariable Integer id
    ) {
        Scenario scenario = scenarioService.loadRest(id);
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario not found");
        }
        return responseMapper.toResponse(scenario);
    }

    @PostMapping
    public ScenarioResponse createScenario(
            @Valid
            @RequestBody
            SaveScenarioRequest request
    ) {
        return responseMapper.toResponse(scenarioService.create(request));
    }

    @PutMapping("/{id}")
    public ScenarioResponse updateScenario(
            @PathVariable Integer id,
            @Valid
            @RequestBody
            SaveScenarioRequest request
    ) {
        Scenario scenario = scenarioService.update(id, request);
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario not found");
        }
        return responseMapper.toResponse(scenario);
    }

    @DeleteMapping("/{id}")
    public void deleteScenario(@PathVariable Integer id) {
        scenarioService.deleteRest(id);
    }
}