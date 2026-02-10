package com.caseo.word.factory;
import com.caseo.domain.model.DocumentSet;
import com.caseo.word.blocks.Block;
import com.caseo.word.blocks.image.ImageBlock;
import com.caseo.word.blocks.list.ListBlock;
import com.caseo.word.blocks.table.TableBlock;
import com.caseo.word.blocks.table.TableColumn;
import com.caseo.word.blocks.table.TableRow;
import com.caseo.word.blocks.table.TableSchema;
import com.caseo.word.blocks.text.TextBlock;

import com.caseo.word.layout.HazardTableLayoutService;
import com.caseo.word.strategy.PlaceholderFillStrategy;

import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;

public class UnifiedBlockFactory {

    private final TableBlockFactory tableBlockFactory;
    private final ListBlockFactory listBlockFactory;
    private final PlaceholderFillStrategy placeholderFillStrategy;
    private final ImageBlockFactory imageBlockFactory;
    private final HazardTableLayoutService hazardTableLayoutService;

    // Карта стратегий создания блоков
    private final Map<Class<?>, BiFunction<String, Object, Block>> creators = new HashMap<>();

    public UnifiedBlockFactory(
            TableBlockFactory tableBlockFactory,
            PlaceholderFillStrategy placeholderFillStrategy,
            ImageBlockFactory imageBlockFactory,
            ListBlockFactory listBlockFactory,
            HazardTableLayoutService hazardTableLayoutService
    ) {
        this.tableBlockFactory = tableBlockFactory;
        this.listBlockFactory = listBlockFactory;
        this.placeholderFillStrategy = placeholderFillStrategy;
        this.imageBlockFactory = imageBlockFactory;
        this.hazardTableLayoutService = hazardTableLayoutService;
        initCreators();
    }

    private void initCreators() {
        // Текст
        creators.put(String.class, (key, val) -> new TextBlock(key, (String) val));

        // Списки (String[])
        creators.put(String[].class, (key, val) -> new ListBlock(key, (String[]) val));

        // Картинки (byte[])
        creators.put(byte[].class, (key, val) ->
                new ImageBlock(key, val,  0)
        );
    }

    public List<Block> buildBlocks(DocumentSet documentSet) throws SQLException {
        Map<String, Object> allData = build(documentSet);
        List<Block> blocks = new ArrayList<>();

        for (Map.Entry<String, Object> entry : allData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                if (key.contains("IMAGE")) {
                    blocks.add(new ImageBlock(key, null, 0));
                }
                continue;
            }

            BiFunction<String, Object, Block> creator = creators.get(value.getClass());
            if (creator != null) {
                blocks.add(creator.apply(key, value));
                continue;
            }

            // 2. Обработка списков
            if (value instanceof List<?> list && !list.isEmpty()) {
                Object first = list.getFirst();

                if (first instanceof String[]) {
                    blocks.add(createTableBlock(key, (List<String[]>) list));
                } else if (first instanceof byte[]) {
                    blocks.add(new ImageBlock(key, value, 0));
                }
            }
        }
        return blocks;
    }

    private TableBlock createTableBlock(String key, List<String[]> rows) {
        List<TableRow> tableRows = new ArrayList<>();
        int colCount = rows.getFirst().length;

        for (String[] rowData : rows) {
            Map<String, Object> cells = new HashMap<>();
            for (int i = 0; i < rowData.length; i++) {
                cells.put("COL_" + i, rowData[i]);
            }
            tableRows.add(new TableRow(cells));
        }

        List<TableColumn> columns = new ArrayList<>();
        for (int i = 0; i < colCount; i++) {
            columns.add(new TableColumn("COL_" + i, "", "TEXT"));
        }

        return new TableBlock(key, new TableSchema(columns), tableRows);
    }

    public Map<String, Object> build(DocumentSet documentSet) throws SQLException {
        Map<String, Object> result = new LinkedHashMap<>();

        putAllIfPresent(result, placeholderFillStrategy.build(documentSet));
        putAllIfPresent(result, tableBlockFactory.build(documentSet));
        putAllIfPresent(result, listBlockFactory.build(documentSet));
        putAllIfPresent(result, imageBlockFactory.build(documentSet));
        List<String[]> hazardData = hazardTableLayoutService.getHazardTableData(documentSet);
        if (!hazardData.isEmpty()) {
            result.put("OBJ_HAZARD_TABLE", hazardData);
        }

        return result;
    }

    private void putAllIfPresent(Map<String, Object> target, Map<String, Object> source) {
        if (source != null && !source.isEmpty()) {
            target.putAll(source);
        }
    }
}