package com.caseo.word.blocks.table;

public class TableColumn {
    private final String key; // логический ключ (например: "EQUIPMENT_NAME")
    private final String title; // заголовок колонки
    private final String type; // тип данных (TEXT, NUMBER, MULTILINE и т.д.)


    public TableColumn(String key, String title, String type) {
        this.key = key;
        this.title = title;
        this.type = type;
    }


    public String getKey() {
        return key;
    }


    public String getTitle() {
        return title;
    }


    public String getType() {
        return type;
    }
}
