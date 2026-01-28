package com.caseo.word.blocks.table;

import java.util.Map;

public class TableRow {
    private final Map<String, Object> cells; // key колонки → значение


    public TableRow(Map<String, Object> cells) {
        this.cells = cells;
    }


    public Object get(String columnKey) {
        return cells.get(columnKey);
    }


    public Map<String, Object> getCells() {
        return cells;
    }
}
