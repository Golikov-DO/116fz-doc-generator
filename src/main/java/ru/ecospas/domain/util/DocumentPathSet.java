package ru.ecospas.domain.util;

import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class DocumentPathSet {

    private DocumentPathSet() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ===== OUTPUT =====
    private static final String OUTPUT_DIR = System.getProperty("user.home") + "/documents";

    // ===== TEMPLATES =====
    //public static final String PLACEHOLDER_TEMPLATE_PATH = "template/template.docx";
    public static final String TAG_TEMPLATE_PATH = "src/main/webapp/WEB-INF/template/tagtemplate.docx";


    // ===== OUTPUT FILE =====
    public static Path buildOutputFile(Organization org, ObjectModel object,
                                       List<ObjectModel> allObjects) throws IOException {

        Path dirPath = Path.of(OUTPUT_DIR, org.getOrganizationShortName());
        Files.createDirectories(dirPath);

        String typeName = object.getType().getType();

        // filter objects of the same type
        var sameTypeObjects = allObjects.stream()
                .filter(o -> o.getType() != null
                        && o.getType().getType().equals(typeName))
                .sorted(Comparator.comparing(ObjectModel::getId))
                .toList();

        int index = 1;

        for (int i = 0; i < sameTypeObjects.size(); i++) {
            if (sameTypeObjects.get(i).getId().equals(object.getId())) {
                index = i + 1;
                break;
            }
        }

        String baseName = typeName + " ПМЛЛПА";
        String fileName = (index == 1)
                ? baseName + ".docx"
                : baseName + " " + index + ".docx";

        return dirPath.resolve(fileName);
    }
}