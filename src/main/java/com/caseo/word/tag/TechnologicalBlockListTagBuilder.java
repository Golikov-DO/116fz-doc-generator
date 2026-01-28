package com.caseo.word.tag;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.TechnologicalBlock;
import com.caseo.domain.service.ObjectService;
import com.caseo.domain.service.TechnologicalBlockService;

import java.sql.SQLException;
import java.util.*;

public class TechnologicalBlockListTagBuilder {

    private final ObjectService objectService;
    private final TechnologicalBlockService technologicalBlockService;

    public TechnologicalBlockListTagBuilder(
            ObjectService objectService,
            TechnologicalBlockService technologicalBlockService
    ) {
        this.objectService = objectService;
        this.technologicalBlockService = technologicalBlockService;
    }

    public Map<String, Object> build(DocumentSet documentSet) throws SQLException {

        Map<String, Object> data = new HashMap<>();

        ObjectModel objectModel =
                objectService.getByOrgId(documentSet.getOrgId());

        List<TechnologicalBlock> technoList =
                technologicalBlockService.getByObject(objectModel.getId());

        if (technoList == null || technoList.isEmpty()) {
            return data;
        }

        String[] items = new String[technoList.size()];

        for (int i = 0; i < technoList.size(); i++) {
            items[i] = technoList.get(i).getName();
        }

        data.put("TECHNO_BLOCK_LIST№", items);

        return data;
    }
}