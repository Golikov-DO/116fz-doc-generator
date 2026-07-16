package ru.ecospas.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ecospas.domain.service.StructureScenarioService;
import ru.ecospas.web.dto.request.scenario.AssignStructureScenariosRequest;
import ru.ecospas.web.dto.request.scenario.EditStructureScenariosResponse;
import ru.ecospas.web.dto.response.scenario.StructureScenariosResponse;

@RestController
@RequestMapping("/api/structures/{structureId}/scenarios")
@RequiredArgsConstructor
public class StructureScenarioRestController {

    private final StructureScenarioService structureScenarioService;

    @GetMapping
    public StructureScenariosResponse getScenarios(@PathVariable Integer structureId) {
        return structureScenarioService.load(structureId);
    }

    @PutMapping
    public void saveScenarios(
            @PathVariable Integer structureId,
            @RequestBody AssignStructureScenariosRequest request
    ) {
        structureScenarioService.save(structureId, request);
    }

    @GetMapping("/edit")
    public EditStructureScenariosResponse edit(@PathVariable Integer structureId) {
        return structureScenarioService.edit(structureId);
    }
}