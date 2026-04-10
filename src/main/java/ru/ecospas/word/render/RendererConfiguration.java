package ru.ecospas.word.render;

import ru.ecospas.word.render.docx4j.Docx4jImageBlockRenderer;
import ru.ecospas.word.render.docx4j.Docx4jListBlockRenderer;
import ru.ecospas.word.render.docx4j.Docx4jTableBlockRenderer;
import ru.ecospas.word.render.docx4j.Docx4jTextBlockRenderer;

import java.util.Arrays;

public class RendererConfiguration {

    public static RendererRegistry createRegistry() {

        return new RendererRegistry(
                Arrays.asList(
                        new Docx4jTextBlockRenderer(),
                        new Docx4jImageBlockRenderer(),
                        new Docx4jListBlockRenderer(),
                        new Docx4jTableBlockRenderer()
                )
        );
    }
}