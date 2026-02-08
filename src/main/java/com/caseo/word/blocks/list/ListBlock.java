package com.caseo.word.blocks.list;

import com.caseo.word.blocks.Block;

public record ListBlock(String key, String[] items) implements Block {

}