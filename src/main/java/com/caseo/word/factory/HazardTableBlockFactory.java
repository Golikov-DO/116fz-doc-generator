package com.caseo.word.factory;

import com.caseo.domain.model.*;
import com.caseo.domain.service.HazardService;
import com.caseo.domain.service.ObjectService;
import com.caseo.word.blocks.table.TableBlock;
import com.caseo.word.blocks.table.TableRow;
import com.caseo.word.blocks.table.TableSchema;
import com.caseo.word.blocks.table.TableColumn;
import com.caseo.word.layout.HazardTableLayoutService;

import java.sql.SQLException;
import java.util.*;

public class HazardTableBlockFactory {

    private final ObjectService objectService;
    private final HazardService hazardService;
    private final HazardTableLayoutService layoutService;

    public HazardTableBlockFactory(
            ObjectService objectService,
            HazardService hazardService,
            HazardTableLayoutService layoutService
    ) {
        this.objectService = objectService;
        this.hazardService = hazardService;
        this.layoutService = layoutService;
    }

    public TableBlock build(DocumentSet documentSet) throws SQLException {

        ObjectModel obj =
                objectService.getByOrgId(documentSet.getOrgId());

        int substanceId = obj.getHazardousSubstanceId();

        // === ДАННЫЕ ===
        List<HazardousParam> params =
                hazardService.getAllParamsOrdered();

        Map<Integer, HazardousParamValue> values =
                hazardService.getValuesBySubstance(substanceId);

        // === LAYOUT ===
        List<Map<String, String>> visualRows =
                layoutService.buildVisualRows(params, values);

        // === SCHEMA ===
        TableSchema schema = new TableSchema(List.of(
                new TableColumn("SECTION", "Раздел", "TEXT"),
                new TableColumn("NAME", "Наименование", "TEXT"),
                new TableColumn("VALUE", "Значение", "TEXT"),
                new TableColumn("SOURCE", "Источник", "TEXT")
        ));

        // === ROWS ===
        List<TableRow> rows = new ArrayList<>();

        for (Map<String,String> vr : visualRows) {
            rows.add(new TableRow(Map.of(
                    "SECTION", vr.get("section"),
                    "NAME", vr.get("name"),
                    "VALUE", vr.get("value"),
                    "SOURCE", vr.get("source")
            )));
        }

        return new TableBlock("HAZARD_TABLE", schema, rows);
    }
}