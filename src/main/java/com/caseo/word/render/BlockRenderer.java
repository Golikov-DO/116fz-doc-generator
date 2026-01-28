package com.caseo.word.render;

import com.caseo.word.blocks.Block;
import org.docx4j.openpackaging.exceptions.Docx4JException;

public interface BlockRenderer<T extends Block> {

    void render(T block, RenderContext ctx) throws Docx4JException;
}