package ru.ecospas.word.blocks.list;

import ru.ecospas.word.blocks.Block;

public record ListBlock(String key, String[] items) implements Block {

}