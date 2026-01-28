package com.caseo.word.blocks.image;

import com.caseo.word.blocks.Block;

public class ImageBlock implements Block {

    private final String key;     // placeholder
    private final byte[] data;    // blob из БД
    private final int width;      // emu/px
    private final int height;     // emu/px
    private final int pictureType; // Document.PICTURE_TYPE_*

    public ImageBlock(String key, byte[] data, int width, int height, int pictureType) {
        this.key = key;
        this.data = data;
        this.width = width;
        this.height = height;
        this.pictureType = pictureType;
    }

    @Override
    public String getKey() {
        return key;
    }

    public byte[] getData() {
        return data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPictureType() {
        return pictureType;
    }
}