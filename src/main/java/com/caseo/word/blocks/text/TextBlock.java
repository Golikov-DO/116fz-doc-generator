package com.caseo.word.blocks.text;

import com.caseo.word.blocks.Block;

/**
 * @param key  ключ/плейсхолдер (опционально)
 * @param text текст
 */
public record TextBlock(String key, String text) implements Block {

}