package ru.ecospas.word.layout;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.service.HazardousSubstanceQueryService;
import ru.ecospas.web.dto.HazardParamDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static ru.ecospas.word.util.LayoutUtil.calcLines;
import static ru.ecospas.word.util.LayoutUtil.rootSection;

@Service
@RequiredArgsConstructor
public class HazardTableLayoutService {

    private static final int NAME_LIMIT = 26;
    private static final int VALUE_LIMIT = 20;
    private static final int SECTION_LIMIT = 5;

    private final ObjectModelRepository objectRepository;
    private final HazardousSubstanceQueryService hazardousSubstanceQueryService;

    public List<String[]> getHazardTableData(int objectId) {

        ObjectModel obj = objectRepository.findById(objectId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Object not found: " + objectId));

        List<HazardParamDto> params =
                hazardousSubstanceQueryService.getHazardParamsWithValues(
                        obj.getHazardousSubstance().getId()
                );

        return buildVisualRowsAsArray(params);
    }

    private List<String[]> buildVisualRowsAsArray(List<HazardParamDto> params) {

        Map<Integer, List<HazardParamDto>> grouped = params.stream()
                .collect(Collectors.groupingBy(
                        p -> rootSection(p.getSection()),
                        TreeMap::new,
                        Collectors.toList()
                ));

        List<String[]> tableRows = new ArrayList<>();

        for (List<HazardParamDto> group : grouped.values()) {

            List<TableRowModel> rows = new ArrayList<>();

            // ---------- STEP 1 ----------
            for (HazardParamDto dto : group) {

                TableRowModel tableRow = new TableRowModel();

                tableRow.isRoot = !dto.getSection().contains(".");
                tableRow.section = dto.getSection();
                tableRow.name = dto.getTitle() + ":";
                tableRow.value = dto.getValue();
                tableRow.source = dto.getSource();

                rows.add(tableRow);
            }

            // ---------- STEP 2 ----------
            for (TableRowModel tableRowModel : rows) {
                tableRowModel.nameLines = calcLines(tableRowModel.name, NAME_LIMIT);
                tableRowModel.valueLines = calcLines(tableRowModel.value, VALUE_LIMIT);

                int secLines = calcLines(tableRowModel.section, SECTION_LIMIT);
                tableRowModel.visualLines = Math.max(Math.max(tableRowModel.nameLines,
                        tableRowModel.valueLines), secLines);
            }

            // ---------- STEP 3 ----------
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
                    secBuf.repeat("\n", Math.max(
                            0, tableRowModel.visualLines - currentSecLines));
                }

                if (tableRowModel.nameLines < tableRowModel.visualLines) {
                    nameBuf.repeat("\n", Math.max(
                            0, tableRowModel.visualLines - tableRowModel.nameLines));
                }

                if (tableRowModel.valueLines < tableRowModel.visualLines) {
                    valueBuf.repeat("\n", Math.max(
                            0, tableRowModel.visualLines - tableRowModel.valueLines));
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