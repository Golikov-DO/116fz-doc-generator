package com.caseo.document;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.Organization;
import com.caseo.domain.service.OrganizationService;
import com.caseo.domain.util.DocumentPathUtil;
import com.caseo.word.blocks.Block;
import com.caseo.word.blocks.image.ImageBlock;
import com.caseo.word.blocks.list.ListBlock;
import com.caseo.word.blocks.table.TableBlock;
import com.caseo.word.blocks.text.TextPlaceholdersBlock;
import com.caseo.word.document.DocumentBuilder;
import com.caseo.word.pipeline.BlockPipeline;
import com.caseo.word.render.RenderContext;
import com.caseo.word.render.RendererRegistry;
import com.caseo.word.factory.*;

import com.caseo.word.render.word.placeholder.WordImageBlockRenderer;
import com.caseo.word.render.word.placeholder.WordListBlockRenderer;
import com.caseo.word.render.word.placeholder.WordTableBlockRenderer;
import com.caseo.word.render.word.placeholder.WordTextPlaceholderRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.List;

public class DocumentGenerator {
    private final OrganizationService organizationService;

    private final TextBlockFactory textBlockFactory;
    private final ListBlockFactory listBlockFactory;
    private final HazardTableBlockFactory hazardTableBlockFactory;
    private final SimpleTableBlockFactory simpleTableBlockFactory;
    private final ImageBlockFactory imageBlockFactory;

    public DocumentGenerator(
            OrganizationService organizationService,
            TextBlockFactory textBlockFactory,
            ListBlockFactory listBlockFactory,
            HazardTableBlockFactory hazardTableBlockFactory,
            ImageBlockFactory imageBlockFactory,
            SimpleTableBlockFactory simpleTableBlockFactory
    ) {
        this.organizationService = organizationService;
        this.textBlockFactory = textBlockFactory;
        this.listBlockFactory = listBlockFactory;
        this.hazardTableBlockFactory = hazardTableBlockFactory;
        this.imageBlockFactory = imageBlockFactory;
        this.simpleTableBlockFactory = simpleTableBlockFactory;
    }

    public void generate(DocumentSet documentSet) throws Exception {

        Organization org = organizationService.getById(documentSet.getId());
        File outFile = DocumentPathUtil.buildOutputFile(org);

        try (InputStream is = new FileInputStream(DocumentPathUtil.PLACEHOLDER_TEMPLATE_PATH);
             XWPFDocument document = new XWPFDocument(is)) {

            RenderContext ctx = new RenderContext(document);

            // ===== REGISTRY =====
            RendererRegistry registry = new RendererRegistry();
            registry.register(TextPlaceholdersBlock.class, new WordTextPlaceholderRenderer());
            registry.register(ListBlock.class, new WordListBlockRenderer());
            registry.register(TableBlock.class, new WordTableBlockRenderer());
            registry.register(ImageBlock.class, new WordImageBlockRenderer());

            // ===== PIPELINE =====
            BlockPipeline pipeline = new BlockPipeline(registry);

            // ===== BLOCKS =====
            DocumentBuilder builder = new DocumentBuilder();

            TextPlaceholdersBlock textBlock = textBlockFactory.build(documentSet);

            // ===== TAG MODE: файл уже создан через docx4j =====
            if (textBlock == null) {
                return;
            }

            // ===== PLACEHOLDER MODE =====
            builder.add(textBlock);

            builder.add(hazardTableBlockFactory.build(documentSet));

            for (ListBlock lb : listBlockFactory.build(documentSet)) {
                builder.add(lb);
            }

            TableBlock simpleTable = simpleTableBlockFactory.build(documentSet);
            if (simpleTable != null) {
                builder.add(simpleTable);
            }

            TableBlock hazardTable =
                    hazardTableBlockFactory.build(documentSet);

            if (hazardTable != null) {
                builder.add(hazardTable);
            }

            ImageBlock image = imageBlockFactory.build(documentSet);
            if (image != null) {
                builder.add(image);
            }

            List<Block> blocks = builder.build();

            // ===== RENDER =====
            pipeline.renderDocument(blocks, ctx);

            // ===== SAVE =====
            try (OutputStream os = new FileOutputStream(outFile)) {
                document.write(os);
            }
        }
    }
}