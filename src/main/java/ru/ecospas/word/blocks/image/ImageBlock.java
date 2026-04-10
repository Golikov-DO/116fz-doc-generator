package ru.ecospas.word.blocks.image;

import ru.ecospas.word.blocks.Block;

/**
 * @param key         placeholder
 * @param data        blob из БД
 * @param pictureType Document.PICTURE_TYPE_*
 */
public record ImageBlock(String key, Object data, int pictureType) implements Block {

}