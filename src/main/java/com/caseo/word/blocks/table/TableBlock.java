package com.caseo.word.blocks.table;

import com.caseo.word.blocks.Block;

import java.util.List;

/**
 * @param key    ключ/плейсхолдер таблицы
 * @param schema структура таблицы
 * @param rows   данные
 */
public record TableBlock(String key, TableSchema schema, List<TableRow> rows) implements Block {

}