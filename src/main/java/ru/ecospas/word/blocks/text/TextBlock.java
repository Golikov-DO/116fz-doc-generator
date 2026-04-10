package ru.ecospas.word.blocks.text;

import ru.ecospas.word.blocks.Block;

/**
 * @param key  ключ/плейсхолдер (опционально)
 * @param text текст
 */
public record TextBlock(String key, String text) implements Block {

}