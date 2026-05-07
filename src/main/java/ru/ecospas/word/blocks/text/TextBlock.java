package ru.ecospas.word.blocks.text;

import ru.ecospas.word.blocks.Block;

/**
 * @param key key/placeholder (optional)
 * @param text the text
 */
public record TextBlock(String key, String text) implements Block {

}