package com.caseo.word.layout;

import com.caseo.domain.model.*;
import com.caseo.word.model.TableRowModel;

import java.util.*;
import java.util.stream.Collectors;

import static com.caseo.word.util.LayoutUtils.calcLines;
import static com.caseo.word.util.LayoutUtils.rootSection;

public class HazardTableLayoutService {

    private static final int NAME_LIMIT  = 26;
    private static final int VALUE_LIMIT = 20;
    private static final int SECTION_LIMIT = 5;

    public List<Map<String,String>> buildVisualRows(
            List<HazardousParam> params,
            Map<Integer, HazardousParamValue> values
    ) {

        Map<Integer, List<HazardousParam>> grouped =
                params.stream()
                        .collect(Collectors.groupingBy(
                                p -> rootSection(p.getSectionNo()),
                                TreeMap::new,
                                Collectors.toList()
                        ));

        List<Map<String,String>> result = new ArrayList<>();

        for (List<HazardousParam> group : grouped.values()) {

            List<TableRowModel> rows = new ArrayList<>();

            // ---------- ШАГ 1 ----------
            for (HazardousParam p : group) {

                TableRowModel r = new TableRowModel();

                r.isRoot = !p.getSectionNo().contains(".");
                r.section = p.getSectionNo();
                r.name = (p.getSubtitle() != null)
                        ? p.getSubtitle()
                        : p.getTitle() + ":";

                HazardousParamValue v = values.get(p.getId());

                r.value = (v != null && v.getValueText() != null)
                        ? v.getValueText()
                        : "";

                r.source = (v != null && v.getSourceInfo() != null)
                        ? v.getSourceInfo()
                        : "";

                rows.add(r);
            }

            // ---------- ШАГ 2 ----------
            for (TableRowModel r : rows) {
                r.nameLines  = calcLines(r.name, NAME_LIMIT);
                r.valueLines = calcLines(r.value, VALUE_LIMIT);

                int secLines = calcLines(r.section, SECTION_LIMIT);
                r.visualLines = Math.max(Math.max(r.nameLines, r.valueLines), secLines);
            }

            // ---------- ШАГ 3 ----------
            StringBuilder secBuf   = new StringBuilder();
            StringBuilder nameBuf  = new StringBuilder();
            StringBuilder valueBuf = new StringBuilder();

            for (int i = 0; i < rows.size(); i++) {
                TableRowModel r = rows.get(i);

                secBuf.append(r.section);
                nameBuf.append(r.name);
                valueBuf.append(r.value);

                int currentSecLines = calcLines(r.section, SECTION_LIMIT);
                if (currentSecLines < r.visualLines) {
                    for (int j = 0; j < r.visualLines - currentSecLines; j++) secBuf.append("\n");
                }

                if (r.nameLines < r.visualLines) {
                    for (int j = 0; j < r.visualLines - r.nameLines; j++) nameBuf.append("\n");
                }

                if (r.valueLines < r.visualLines) {
                    for (int j = 0; j < r.visualLines - r.valueLines; j++) valueBuf.append("\n");
                }

                if (i < rows.size() - 1) {
                    secBuf.append("\n");
                    nameBuf.append("\n");
                    valueBuf.append("\n");
                }
            }

            String secText   = secBuf.toString().replaceAll("\\n+$", "");
            String nameText  = nameBuf.toString().replaceAll("\\n+$", "");
            String valueText = valueBuf.toString().replaceAll("\\n+$", "");

            String sourceText = rows.stream()
                    .map(x -> x.source)
                    .filter(s -> s != null && !s.isBlank())
                    .collect(Collectors.joining("\n"));

            Map<String,String> row = new HashMap<>();
            row.put("section", secText);
            row.put("name", nameText);
            row.put("value", valueText);
            row.put("source", sourceText);

            result.add(row);
        }

        return result;
    }
}