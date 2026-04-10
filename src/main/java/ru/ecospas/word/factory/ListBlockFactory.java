package ru.ecospas.word.factory;

import ru.ecospas.domain.model.*;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.domain.util.ObjectTechnicalDescriptionFormatter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBlockFactory {

    private final ParentService<ObjectModel> objectService;
    private final ChildService<ObjectStructure> objectStructureService;
    private final ChildService<ObjectTechnologicalBlock> objectTechnologicalBlockService;
    private final ChildService<ObjectAddress> objectAddressService;
    private final ParentService<ReferenceCity> referenceCityService;

    public ListBlockFactory(
            ParentService<ObjectModel> objectService,
            ChildService<ObjectStructure> objectStructureService,
            ChildService<ObjectTechnologicalBlock> objectTechnologicalBlockService,
            ChildService<ObjectAddress> objectAddressService,
            ParentService<ReferenceCity> referenceCityService
    ) {
        this.objectService = objectService;
        this.objectStructureService = objectStructureService;
        this.objectTechnologicalBlockService = objectTechnologicalBlockService;
        this.objectAddressService = objectAddressService;
        this.referenceCityService = referenceCityService;
    }

    public Map<String,Object> build(int objectId) throws SQLException {

        Map<String,Object> data = new HashMap<>();

        ObjectModel object = objectService.getOneById(objectId);

        // ===== OBJ_AREA_LOCATION (Теперь как LIST без номеров) =====
        String[] descriptionParagraphs = ObjectTechnicalDescriptionFormatter.formatAsParagraphs(object.getCity());

        if (descriptionParagraphs.length > 0) {
            data.put("OBJ_AREA_LOCATION_LIST", descriptionParagraphs);
        }
        // ============================================================

        // ===== OBJECT_STRUCTURE_LIST =====
        List<ObjectStructure> structureList = objectStructureService.getManyByParentId(object.getId());
        String[] structureItems = extractNames(structureList);
        if (structureItems.length > 0) {
            data.put("OBJ_STRUCTURE_LIST1", structureItems);
        }

        // ===== TECHNO_BLOCK_LIST =====
        List<ObjectTechnologicalBlock> technoList = objectTechnologicalBlockService.getManyByParentId(object.getId());
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
            result[i] = items.get(i).getName();
        }
        return result;
    }
}