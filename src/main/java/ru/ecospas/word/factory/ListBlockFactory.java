package ru.ecospas.word.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.repository.ObjectStructureRepository;
import ru.ecospas.domain.repository.ObjectTechnologicalBlockRepository;
import ru.ecospas.domain.repository.ScenarioRepository;
import ru.ecospas.domain.service.ScenarioNumberService;
import ru.ecospas.domain.util.Collect;
import ru.ecospas.domain.util.ObjectTechnicalDescriptionFormatter;
import ru.ecospas.domain.util.SubscriptUtils;
import ru.ecospas.web.dto.response.scenario.ScenarioDTO;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListBlockFactory {

    private final ObjectModelRepository objectRepository;
    private final ObjectStructureRepository objectStructureRepository;
    private final ObjectTechnologicalBlockRepository objectTechnologicalBlockRepository;
    private final ScenarioRepository scenarioRepository;
    private final ScenarioNumberService numberService;

    public Map<String, Object> build(int objectId) throws SQLException {

        Map<String, Object> data = new HashMap<>();

        ObjectModel object = objectRepository.findById(objectId).orElseThrow();

        // ===== OBJ_AREA_LOCATION =====
        String[] descriptionParagraphs = ObjectTechnicalDescriptionFormatter
                .formatAsParagraphs(object.getCity());

        if (descriptionParagraphs.length > 0) {
            data.put("OBJ_AREA_LOCATION_LIST", descriptionParagraphs);
        }
        // ============================================================

        // ===== OBJECT_STRUCTURE_LIST =====
        List<ObjectStructure> structureList = objectStructureRepository
                .findAllByObjectId(object.getId());
        String[] structureItems = extractNames(structureList);
        if (structureItems.length > 0) {
            data.put("OBJ_STRUCTURE_LIST1", structureItems);
        }

        // ===== TECHNO_BLOCK_LIST =====
        List<ObjectTechnologicalBlock> technoList = objectTechnologicalBlockRepository
                .findAllByObjectId(object.getId());
        String[] technoItems = extractNames(technoList);
        if (technoItems.length > 0) {
            data.put("OBJ_TECHNO_BLOCK_LIST№", technoItems);
        }

        // ===== OBJECT SCENARIOS =====
        List<ObjectStructure> structures = objectStructureRepository
                .findAllByObjectId(object.getId());

        LinkedHashSet<Integer> likelyIds = new LinkedHashSet<>();
        LinkedHashSet<Integer> dangerousIds = new LinkedHashSet<>();

        for (ObjectStructure s : structures) {
            Collect.collect(s.getLikelyIds(), likelyIds);
            Collect.collect(s.getDangerousIds(), dangerousIds);
        }

        List<Scenario> allScenarios = scenarioRepository.findAll();
        Map<Integer, Scenario> baseMap = allScenarios.stream()
                .collect(Collectors.toMap(Scenario::getId, s -> s));

        LinkedHashSet<Integer> allIds = new LinkedHashSet<>();
        allIds.addAll(likelyIds);
        allIds.addAll(dangerousIds);

        Map<Integer, ScenarioDTO> scenarioMap =
                numberService.buildFromIds(allIds, baseMap);

        List<String> likelyList = new ArrayList<>();
        List<String> dangerousList = new ArrayList<>();

        for (Integer id : likelyIds) {
            ScenarioDTO dto = scenarioMap.get(id);
            if (dto == null) continue;

            likelyList.add(" – С" + SubscriptUtils.toSubscript(dto.getNumber())
                    + " – " + dto.getName());
        }

        for (Integer id : dangerousIds) {
            ScenarioDTO dto = scenarioMap.get(id);
            if (dto == null) continue;

            dangerousList.add(" – С" + SubscriptUtils.toSubscript(dto.getNumber())
                    + " – " + dto.getName());
        }

        if (!likelyList.isEmpty()) {
            data.put("OBJ_SCENARIO_LIKELY_LIST", likelyList.toArray(new String[0]));
        }

        if (!dangerousList.isEmpty()) {
            data.put("OBJ_SCENARIO_DANGEROUS_LIST", dangerousList.toArray(new String[0]));
        }

        return data;
    }

    private <T extends NumberedItem> String[] extractNames(List<T> items) {

        if (items == null || items.isEmpty()) return new String[0];
        String[] result = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            result[i] = items.get(i).getName();
        }
        return result;
    }
}