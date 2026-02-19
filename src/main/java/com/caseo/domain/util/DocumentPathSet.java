package com.caseo.domain.util;

import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.Organization;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentPathSet {

    // ===== OUTPUT =====
    private static final String OUTPUT_DIR = "result";

    // ===== TEMPLATES =====
    public static final String PLACEHOLDER_TEMPLATE_PATH = "template/template.docx";
    public static final String TAG_TEMPLATE_PATH = "template/tagtemplate.docx";

    // ===== OUTPUT FILE =====
    public static Path buildOutputFile(Organization org, ObjectModel object) throws IOException {
        // 1. Формируем путь к папке: result/НазваниеОрг
        Path dirPath = Path.of(OUTPUT_DIR, org.organizationShortName());

        // 2. Создаем директории, если их еще нет
        Files.createDirectories(dirPath);

        // 3. Формируем имя файла
        String fileName = object.objectShortName() + " ПМЛЛПА.docx";

        // 4. Возвращаем полный путь к файлу
        return dirPath.resolve(fileName);
    }
}