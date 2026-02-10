package com.caseo.word.factory;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.NumberedItem;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.ObjectStructure;
import com.caseo.domain.model.TechnologicalBlock;
import com.caseo.domain.service.*;
import com.caseo.domain.util.TechnicalDescriptionFormatter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBlockFactory {

    private final ObjectService objectService;
    private final ObjectStructureService objectStructureService;
    private final TechnologicalBlockService technologicalBlockService;
    private final ObjectAddressService objectAddressService;
    private final ObjectCityService objectCityService;

    public ListBlockFactory(
            ObjectService objectService,
            ObjectStructureService objectStructureService,
            TechnologicalBlockService technologicalBlockService,
            ObjectAddressService objectAddressService,
            ObjectCityService objectCityService
    ) {
        this.objectService = objectService;
        this.objectStructureService = objectStructureService;
        this.technologicalBlockService = technologicalBlockService;
        this.objectAddressService = objectAddressService;
        this.objectCityService = objectCityService;
    }

    public Map<String,Object> build(DocumentSet documentSet) throws SQLException {

        Map<String,Object> data = new HashMap<>();

        ObjectModel objectModel = objectService.getByOrgId(documentSet.orgId());

        // ===== OBJ_AREA_LOCATION (Теперь как LIST без номеров) =====
        var objAddr = objectAddressService.getByObjectId(objectModel.id());
        var city = objectCityService.getById(objAddr.id());
        // Используем новый метод, возвращающий массив
        String[] descriptionParagraphs = TechnicalDescriptionFormatter.formatAsParagraphs(city);

        if (descriptionParagraphs.length > 0) {
            // Новый тег заканчивается на LIST, как вы и предложили
            data.put("OBJ_AREA_LOCATION_LIST", descriptionParagraphs);
        }
        // ============================================================

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