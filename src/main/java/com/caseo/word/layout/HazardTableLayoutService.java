package com.caseo.word.layout;

import com.caseo.domain.model.*;
import com.caseo.domain.service.ChildService;
import com.caseo.domain.service.ObjectHazardService;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.caseo.word.util.LayoutUtil.calcLines;
import static com.caseo.word.util.LayoutUtil.rootSection;

public class HazardTableLayoutService {
    private final ChildService<ObjectModel> objectService;  // вместо ObjectService
    private final ObjectHazardService objectHazardService;
    private static final int NAME_LIMIT = 26;
    private static final int VALUE_LIMIT = 20;
    private static final int SECTION_LIMIT = 5;

    public HazardTableLayoutService(
            ChildService<ObjectModel> objectService,
            ObjectHazardService objectHazardService) {
        this.objectService = objectService;
        this.objectHazardService = objectHazardService;
    }

    public List<String[]> getHazardTableData(int objectId) throws SQLException {
        // 2. Сбор данных (переехало из HazardTableBlockFactory)
        ObjectModel obj = objectService.getOneByParentId(objectId);
        List<ObjectHazardousParam> params = objectHazardService.getAllParamsOrdered(
                obj.getHazardousSubstance().getId());

        Map<Integer, ObjectHazardousParamValue> values = new HashMap<>();
        for (ObjectHazardousParam param : params) {
            values.putAll(objectHazardService.getValuesByParam(param.getId()));
        }

        // 3. Вызов твоей логики расчета (существующий метод)
        // Но теперь пусть он сразу возвращает List<String[]>
        return buildVisualRowsAsArray(params, values);
    }

    private List<String[]> buildVisualRowsAsArray(
            List<ObjectHazardousParam> params,
            Map<Integer,
            ObjectHazardousParamValue> values) {

        Map<Integer, List<ObjectHazardousParam>> grouped =
                params.stream()
                        .collect(Collectors.groupingBy(
                                p -> rootSection(p.getSectionNo()),
                                TreeMap::new,
                                Collectors.toList()
                        ));

        List<String[]> tableRows = new ArrayList<>();

        for (List<ObjectHazardousParam> group : grouped.values()) {

            List<TableRowModel> rows = new ArrayList<>();

            // ---------- ШАГ 1 ----------
            for (ObjectHazardousParam objectHazardousParam : group) {

                TableRowModel tableRow = new TableRowModel();

                tableRow.isRoot = !objectHazardousParam.getSectionNo().contains(".");
                tableRow.section = objectHazardousParam.getSectionNo();
                tableRow.name = (objectHazardousParam.getSubtitle() != null)
                        ? objectHazardousParam.getSubtitle()
                        : objectHazardousParam.getTitle() + ":";

                ObjectHazardousParamValue objectHazardousParamValue = values.get(
                        objectHazardousParam.getId());

                tableRow.value = (objectHazardousParamValue != null
                        && objectHazardousParamValue.getValueText() != null)
                        ? objectHazardousParamValue.getValueText()
                        : "";

                tableRow.source = (objectHazardousParamValue != null
                        && objectHazardousParamValue.getSourceInfo() != null)
                        ? objectHazardousParamValue.getSourceInfo()
                        : "";

                rows.add(tableRow);
            }

            // ---------- ШАГ 2 ----------
            for (TableRowModel tableRowModel : rows) {
                tableRowModel.nameLines = calcLines(tableRowModel.name, NAME_LIMIT);
                tableRowModel.valueLines = calcLines(tableRowModel.value, VALUE_LIMIT);

                int secLines = calcLines(tableRowModel.section, SECTION_LIMIT);
                tableRowModel.visualLines = Math.max(Math.max(tableRowModel.nameLines,
                        tableRowModel.valueLines), secLines);
            }

            // ---------- ШАГ 3 ----------
            StringBuilder secBuf = new StringBuilder();
            StringBuilder nameBuf = new StringBuilder();
            StringBuilder valueBuf = new StringBuilder();

            for (int i = 0; i < rows.size(); i++) {
                TableRowModel tableRowModel = rows.get(i);

                secBuf.append(tableRowModel.section);
                nameBuf.append(tableRowModel.name);
                valueBuf.append(tableRowModel.value);

                int currentSecLines = calcLines(tableRowModel.section, SECTION_LIMIT);
                if (currentSecLines < tableRowModel.visualLines) {
                    secBuf.append("\n".repeat(Math.max(
                            0, tableRowModel.visualLines - currentSecLines)));
                }

                if (tableRowModel.nameLines < tableRowModel.visualLines) {
                    nameBuf.append("\n".repeat(Math.max(
                            0, tableRowModel.visualLines - tableRowModel.nameLines)));
                }

                if (tableRowModel.valueLines < tableRowModel.visualLines) {
                    valueBuf.append("\n".repeat(Math.max(
                            0, tableRowModel.visualLines - tableRowModel.valueLines)));
                }

                if (i < rows.size() - 1) {
                    secBuf.append("\n");
                    nameBuf.append("\n");
                    valueBuf.append("\n");
                }
            }

            String secText = secBuf.toString().replaceAll("\\n+$", "");
            String nameText = nameBuf.toString().replaceAll("\\n+$", "");
            String valueText = valueBuf.toString().replaceAll("\\n+$", "");

            String sourceText = rows.stream()
                    .map(rowModel -> rowModel.source)
                    .filter(string -> string != null && !string.isBlank())
                    .collect(Collectors.joining("\n"));

            tableRows.add(new String[]{secText, nameText, valueText, sourceText});
        }

        return tableRows;
    }
}