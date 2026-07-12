package ru.ecospas.word.pipeline;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import ru.ecospas.word.blocks.Block;

import java.util.List;

public record OpenResult(WordprocessingMLPackage document, List<Block> blocks) {

}