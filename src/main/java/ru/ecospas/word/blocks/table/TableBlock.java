package ru.ecospas.word.blocks.table;

import ru.ecospas.word.blocks.Block;

import java.util.List;

/**
 * @param key    ключ/плейсхолдер таблицы
 * @param schema структура таблицы
 * @param rows   данные
 */
public record TableBlock(String key, TableSchema schema, List<TableRow> rows) implements Block {

}