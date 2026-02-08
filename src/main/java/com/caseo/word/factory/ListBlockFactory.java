package com.caseo.word.factory;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.NumberedItem;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.ObjectStructure;
import com.caseo.domain.model.TechnologicalBlock;
import com.caseo.domain.service.ObjectService;
import com.caseo.domain.service.ObjectStructureService;
import com.caseo.domain.service.TechnologicalBlockService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBlockFactory {

    private final ObjectService objectService;
    private final ObjectStructureService objectStructureService;
    private final TechnologicalBlockService technologicalBlockService;

    public ListBlockFactory(
            ObjectService objectService,
            ObjectStructureService objectStructureService,
            TechnologicalBlockService technologicalBlockService
    ) {
        this.objectService = objectService;
        this.objectStructureService = objectStructureService;
        this.technologicalBlockService = technologicalBlockService;
    }

    public Map<String,Object> build(DocumentSet documentSet) throws SQLException {

        Map<String,Object> data = new HashMap<>();

        ObjectModel objectModel = objectService.getByOrgId(documentSet.orgId());

        // ===== OBJECT_STRUCTURE_LIST =====
        List<ObjectStructure> structureList = objectStructureService.getByObject(objectModel.id());
        String[] structureItems = extractNames(structureList);
        if (structureItems.length > 0) {
            data.put("OBJ_STRUCTURE_LIST1", structureItems);
        }

        // ===== TECHNO_BLOCK_LIST =====
        List<TechnologicalBlock> technoList = technologicalBlockService.getByObject(objectModel.id());
        String[] technoItems = extractNames(technoList);
        if (technoItems.length > 0) {
            data.put("OBJ_TECHNO_BLOCK_LIST№", technoItems);
        }
        return data;
    }

    private <T extends NumberedItem> String[] extractNames(List<T> items) {

        if (items == null || items.isEmpty()) return new String[0];
        String[] result = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            result[i] = items.get(i).name();
        }
        return result;
    }
}