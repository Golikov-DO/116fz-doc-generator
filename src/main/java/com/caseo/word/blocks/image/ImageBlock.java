package com.caseo.word.blocks.image;

import com.caseo.word.blocks.Block;

/**
 * @param key         placeholder
 * @param data        blob из БД
 * @param pictureType Document.PICTURE_TYPE_*
 */
public record ImageBlock(String key, Object data, int pictureType) implements Block {

}