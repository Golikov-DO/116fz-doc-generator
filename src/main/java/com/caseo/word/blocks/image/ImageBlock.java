package com.caseo.word.blocks.image;

import com.caseo.word.blocks.Block;

/**
 * @param key         placeholder
 * @param data        blob из БД
 * @param width       emu/px
 * @param height      emu/px
 * @param pictureType Document.PICTURE_TYPE_*
 */
public record ImageBlock(String key, byte[] data, int width, int height, int pictureType) implements Block {

}