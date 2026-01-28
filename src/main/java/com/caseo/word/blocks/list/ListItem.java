package com.caseo.word.blocks.list;

import com.caseo.word.ListFormat;

public class ListItem {

    private final String text;        // текст элемента
    private final ListFormat format;  // формат маркера (enum)

    public ListItem(String text, ListFormat format) {
        this.text = text;
        this.format = format;
    }

    public String getText() {
        return text;
    }

    public ListFormat getFormat() {
        return format;
    }
}
