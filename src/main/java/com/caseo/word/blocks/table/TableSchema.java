package com.caseo.word.blocks.table;

import java.util.List;

public class TableSchema {
    private final List<TableColumn> columns;


    public TableSchema(List<TableColumn> columns) {
        this.columns = columns;
    }


    public List<TableColumn> getColumns() {
        return columns;
    }
}
