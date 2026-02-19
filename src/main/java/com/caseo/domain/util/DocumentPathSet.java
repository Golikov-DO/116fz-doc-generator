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
        Path dirPath = Path.of(OUTPUT_DIR, org.organizationShortName());
        Files.createDirectories(dirPath);

        String name = object.objectShortName();
        Path filePath = dirPath.resolve(name + " ПМЛЛПА.docx");

        int count = 2;
        // Если файл есть — мы его просто сносим.
        // Если это "старый" файл — место станет свободным.
        // Если мы уже создали такой файл в этом цикле — место тоже станет свободным (перезапись).
        while (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
                // Удалили? Значит имя свободно, выходим из цикла и возвращаем filePath
                break;
            } catch (IOException e) {
                // Если файл удалить нельзя (например, он открыт в Word),
                // тогда и только тогда идем на следующий номер
                filePath = dirPath.resolve(name + " " + count + " ПМЛЛПА.docx");
                count++;
            }
        }

        return filePath;
    }
}