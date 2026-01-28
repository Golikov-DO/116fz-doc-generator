package com.caseo.domain.util;

import com.caseo.domain.model.Organization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentPathUtil {

    // ===== OUTPUT =====
    private static final String OUTPUT_DIR = "result";

    // ===== TEMPLATES =====
    public static final String PLACEHOLDER_TEMPLATE_PATH = "template/template.docx";
    public static final String TAG_TEMPLATE_PATH = "template/tagtemplate.docx";

    // ===== FILE NAME =====
    public static String buildFileName(Organization org) {
        return org.getOrganizationShortName() + " ПМЛЛПА.docx";
    }

    // ===== OUTPUT FILE =====
    public static File buildOutputFile(Organization org) throws IOException {
        Path dir = Path.of(OUTPUT_DIR);
        Files.createDirectories(dir);
        return dir.resolve(buildFileName(org)).toFile();
    }
}