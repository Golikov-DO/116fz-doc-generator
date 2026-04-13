package ru.ecospas.word.factory;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.word.blocks.Block;
import ru.ecospas.word.blocks.image.ImageBlock;
import ru.ecospas.word.blocks.list.ListBlock;
import ru.ecospas.word.blocks.table.TableBlock;
import ru.ecospas.word.blocks.table.TableColumn;
import ru.ecospas.word.blocks.table.TableRow;
import ru.ecospas.word.blocks.table.TableSchema;
import ru.ecospas.word.blocks.text.TextBlock;

import ru.ecospas.word.layout.ContactTableLayoutService;
import ru.ecospas.word.layout.HazardTableLayoutService;
import ru.ecospas.word.strategy.PlaceholderFillStrategy;

import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;

public class UnifiedBlockFactory {

    private final TableBlockFactory tableBlockFactory;
    private final ListBlockFactory listBlockFactory;
    private final PlaceholderFillStrategy placeholderFillStrategy;
    private final ImageBlockFactory imageBlockFactory;
    private final HazardTableLayoutService hazardTableLayoutService;
    private final ContactTableLayoutService contactTableLayoutService;
    private final ParentService<ObjectModel> objectService;

    // Map of block creation strategies
    private final Map<Class<?>, BiFunction<String, Object, Block>> creators = new HashMap<>();

    public UnifiedBlockFactory(
            TableBlockFactory tableBlockFactory,
            PlaceholderFillStrategy placeholderFillStrategy,
            ImageBlockFactory imageBlockFactory,
            ListBlockFactory listBlockFactory,
            HazardTableLayoutService hazardTableLayoutService,
            ContactTableLayoutService contactTableLayoutService,
            ParentService<ObjectModel> objectService
    ) {
        this.tableBlockFactory = tableBlockFactory;
        this.listBlockFactory = listBlockFactory;
        this.placeholderFillStrategy = placeholderFillStrategy;
        this.imageBlockFactory = imageBlockFactory;
        this.hazardTableLayoutService = hazardTableLayoutService;
        this.contactTableLayoutService = contactTableLayoutService;
        this.objectService = objectService;
        initCreators();
    }

    private void initCreators() {
        // Text
        creators.put(String.class, (key, val) -> new TextBlock(key, (String) val));

        // Lists (String[])
        creators.put(String[].class, (key, val) -> new ListBlock(key, (String[]) val));

        // Pictures (byte[])
        creators.put(byte[].class, (key, val) ->
                new ImageBlock(key, val, resolvePictureType(key))
        );
    }

    public List<Block> buildBlocks(int objectId) throws SQLException {
        Map<String, Object> allData = build(objectId);
        List<Block> blocks = new ArrayList<>();

        for (Map.Entry<String, Object> entry : allData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                if (key.contains("IMAGE")) {
                    blocks.add(new ImageBlock(key, null, resolvePictureType(key)));
                } else if (key.contains("TABLE")) {
                    blocks.add(new TableBlock(key, null, null));
                }
                continue;
            }

            BiFunction<String, Object, Block> creator = creators.get(value.getClass());
            if (creator != null) {
                blocks.add(creator.apply(key, value));
                continue;
            }

            // 2. List Processing

            if (value instanceof List<?> list && !list.isEmpty()) {
                Object first = list.getFirst();

                if (first instanceof String[]) {
                    blocks.add(createTableBlock(key, (List<String[]>) list));
                } else if (first instanceof byte[]) {
                    blocks.add(new ImageBlock(key, value, resolvePictureType(key)));
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

    public Map<String, Object> build(int objectId) throws SQLException {
        Map<String, Object> result = new LinkedHashMap<>();

        ObjectModel obj = objectService.getOneById(objectId);
        if (obj == null) {
            return result;
        }
        int orgId = obj.getOrganization().getId();

        putAllIfPresent(result, placeholderFillStrategy.build(orgId, objectId));
        putAllIfPresent(result, tableBlockFactory.build(objectId));
        putAllIfPresent(result, listBlockFactory.build(objectId));
        putAllIfPresent(result, imageBlockFactory.build(objectId));
        List<String[]> hazardData = hazardTableLayoutService.getHazardTableData(objectId);
        if (!hazardData.isEmpty()) result.put("OBJ_TABLE_2_PLACEHOLDER", hazardData);
        List<String[]> contactData = contactTableLayoutService.getContactTableData(orgId, objectId);
        if (!contactData.isEmpty()) result.put("OBJ_TABLE_6_PLACEHOLDER", contactData);

        return result;
    }

    private void putAllIfPresent(Map<String, Object> target, Map<String, Object> source) {
        if (source != null && !source.isEmpty()) {
            target.putAll(source);
        }
    }

    private int resolvePictureType(String key) {

        if (key.startsWith("ASF_IMAGE")) return 0;
        if (key.startsWith("OBJ_IMAGE")) return 1;
        return 0;
    }
}