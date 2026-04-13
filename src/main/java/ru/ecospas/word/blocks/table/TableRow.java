package ru.ecospas.word.blocks.table;

import java.util.Map;

/**
 * @param cells key columns → value
 */
public record TableRow(Map<String, Object> cells) {

    public Object get(String columnKey) {
        return cells.get(columnKey);
    }
}
