package com.caseo.word.blocks.table;

import com.caseo.word.blocks.Block;

import java.util.List;

public class TableBlock implements Block {

    private final String key;               // ключ/плейсхолдер таблицы
    private final TableSchema schema;       // структура таблицы
    private final List<TableRow> rows;      // данные

    public TableBlock(String key, TableSchema schema, List<TableRow> rows) {
        this.key = key;
        this.schema = schema;
        this.rows = rows;
    }

    @Override
    public String getKey() {
        return key;
    }

    public TableSchema getSchema() {
        return schema;
    }

    public List<TableRow> getRows() {
        return rows;
    }
}