package com.caseo.word.tag;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.ObjectStructureP1;
import com.caseo.domain.service.ObjectService;
import com.caseo.domain.service.ObjectStructureService;

import java.sql.SQLException;
import java.util.*;

public class ObjectStructureListTagBuilder {

    private final ObjectService objectService;
    private final ObjectStructureService objectStructureService;

    public ObjectStructureListTagBuilder(
            ObjectService objectService,
            ObjectStructureService objectStructureService
    ) {
        this.objectService = objectService;
        this.objectStructureService = objectStructureService;
    }

    public Map<String, Object> build(DocumentSet documentSet) throws SQLException {

        Map<String, Object> data = new HashMap<>();

        ObjectModel objectModel =
                objectService.getByOrgId(documentSet.getOrgId());

        List<ObjectStructureP1> structureList =
                objectStructureService.getByObject(objectModel.getId());

        if (structureList == null || structureList.isEmpty()) {
            return data;
        }

        // String[] для LIST
        String[] items = new String[structureList.size()];

        for (int i = 0; i < structureList.size(); i++) {
            items[i] = structureList.get(i).getName();
        }

        // КЛЮЧ = ТЭГ
        data.put("OBJECT_STRUCTURE_LIST1", items);

        return data;
    }
}