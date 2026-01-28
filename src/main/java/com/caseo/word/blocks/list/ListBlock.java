package com.caseo.word.blocks.list;

import com.caseo.word.blocks.Block;

import java.util.List;

public class ListBlock implements Block {

    private final String key;
    private final List<ListItem> items;

    public ListBlock(String key, List<ListItem> items) {
        this.key = key;
        this.items = items;
    }

    public String getKey() {
        return key;
    }

    public List<ListItem> getItems() {
        return items;
    }
}