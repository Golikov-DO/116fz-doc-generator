package com.caseo.word.factory;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.NumberedItem;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.ObjectStructureP1;
import com.caseo.domain.model.TechnologicalBlock;
import com.caseo.domain.service.ObjectService;
import com.caseo.domain.service.ObjectStructureService;
import com.caseo.domain.service.TechnologicalBlockService;
import com.caseo.word.ListFormat;
import com.caseo.word.blocks.list.ListBlock;
import com.caseo.word.blocks.list.ListItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Создаёт два блока:
     *  - OBJECT_STRUCTURE_LIST
     *  - TECHNO_BLOCK_LIST
     */
    public List<ListBlock> build(DocumentSet documentSet) throws SQLException {

        ObjectModel objectModel =
                objectService.getByOrgId(documentSet.getOrgId());

        // ===== OBJECT_STRUCTURE_LIST =====
        List<ObjectStructureP1> structureList =
                objectStructureService.getByObject(objectModel.getId());

        List<ListItem> structureItems =
                buildItems(structureList, ListFormat.DOT);

        ListBlock objectStructureBlock = new ListBlock(
                "OBJECT_STRUCTURE_LIST",
                structureItems
        );

        // ===== TECHNO_BLOCK_LIST =====
        List<TechnologicalBlock> technoList =
                technologicalBlockService.getByObject(objectModel.getId());

        List<ListItem> technoItems =
                buildItems(technoList, ListFormat.NO_SIGN);

        ListBlock technoBlock = new ListBlock(
                "TECHNO_BLOCK_LIST",
                technoItems
        );

        return List.of(objectStructureBlock, technoBlock);
    }

    private <T extends NumberedItem> List<ListItem> buildItems(
            List<T> items,
            ListFormat format
    ) {
        List<ListItem> result = new ArrayList<>();

        if (items == null) return result;

        for (T item : items) {
            result.add(new ListItem(
                    item.getName(),
                    format
            ));
        }

        return result;
    }
}