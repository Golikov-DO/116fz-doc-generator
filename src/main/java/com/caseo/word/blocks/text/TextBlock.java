package com.caseo.word.blocks.text;

import com.caseo.word.blocks.Block;

public class TextBlock implements Block {

    private final String key;   // ключ/плейсхолдер (опционально)
    private final String text;  // текст

    public TextBlock(String key, String text) {
        this.key = key;
        this.text = text;
    }

    @Override
    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }
}