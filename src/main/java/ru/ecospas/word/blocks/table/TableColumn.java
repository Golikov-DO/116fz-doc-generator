package ru.ecospas.word.blocks.table;

/**
 * @param key boolean key (for example: "EQUIPMENT_NAME")
 * @param title column title
 * @param type data type (TEXT, NUMBER, MULTILINE, etc.)
 */
public record TableColumn(String key, String title, String type) {
}
