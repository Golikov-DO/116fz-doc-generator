package com.caseo.word.blocks.table;

/**
 * @param key   логический ключ (например: "EQUIPMENT_NAME")
 * @param title заголовок колонки
 * @param type  тип данных (TEXT, NUMBER, MULTILINE и т.д.)
 */
public record TableColumn(String key, String title, String type) {
}
