package com.caseo.word.strategy;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.Organization;
import com.caseo.domain.service.OrganizationService;
import com.caseo.domain.util.DocumentPathUtil;
import com.caseo.word.blocks.text.TextPlaceholderService;
import com.caseo.word.render.word.docx4j.Docx4jTagProcessor;
import com.caseo.word.tag.*;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TagFillStrategyDocx4j implements FillStrategy {

    private final TextPlaceholderService textPlaceholderService;
    private final Docx4jTagProcessor processor;
    private final Table1TagDataBuilder table1TagDataBuilder;
    private final ObjectStructureListTagBuilder objectStructureListTagBuilder;
    private final TechnologicalBlockListTagBuilder technologicalBlockListTagBuilder;
    private final ObjectSchemeImageTagBuilder objectSchemeImageTagBuilder;
    private final OrganizationService organizationService;


    private final String templatePath;

    public TagFillStrategyDocx4j(
            OrganizationService organizationService,
            TextPlaceholderService textPlaceholderService,
            Table1TagDataBuilder table1TagDataBuilder,
            ObjectStructureListTagBuilder objectStructureListTagBuilder,
            TechnologicalBlockListTagBuilder technologicalBlockListTagBuilder,
            ObjectSchemeImageTagBuilder objectSchemeImageTagBuilder,
            String templatePath
    ) {
        this.organizationService = organizationService;
        this.textPlaceholderService = textPlaceholderService;
        this.table1TagDataBuilder = table1TagDataBuilder;
        this.objectStructureListTagBuilder = objectStructureListTagBuilder;
        this.technologicalBlockListTagBuilder = technologicalBlockListTagBuilder;
        this.objectSchemeImageTagBuilder = objectSchemeImageTagBuilder;
        this.processor = new Docx4jTagProcessor();
        this.templatePath = templatePath;
    }

    @Override
    public Map<String, Object> build(DocumentSet documentSet) throws SQLException {
        // те же данные, просто приводим тип
        return new HashMap<>(textPlaceholderService.build(documentSet));
    }

    public void generate(DocumentSet documentSet) throws Exception {

        Map<String, Object> data = build(documentSet);

        // добавляем таблицу
        data.putAll(table1TagDataBuilder.build(documentSet));

        // добавляем списки
        data.putAll(objectStructureListTagBuilder.build(documentSet));
        data.putAll(technologicalBlockListTagBuilder.build(documentSet));
        data.putAll(objectSchemeImageTagBuilder.build(documentSet));

        File template = new File(templatePath);
        Organization org = organizationService.getById(documentSet.getId());

        File output = DocumentPathUtil.buildOutputFile(org);

        processor.process(template, output, data);

        processor.process(template, output, data);
    }
}