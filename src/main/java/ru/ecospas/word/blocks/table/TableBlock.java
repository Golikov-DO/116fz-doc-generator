package ru.ecospas.word.blocks.table;

import ru.ecospas.word.blocks.Block;

import java.util.List;

/**
 * @param key table key/placeholder
 * @param schema table structure
 * @param rows data
 */
public record TableBlock(String key, TableSchema schema, List<TableRow> rows) implements Block {

}