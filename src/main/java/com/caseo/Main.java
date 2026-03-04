package com.caseo;

import com.caseo.app.ApplicationContext;
import com.caseo.app.Bootstrap;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.Organization;
import com.caseo.domain.util.DocumentPathSet;
import com.caseo.word.strategy.FillStrategy;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws Exception {

        ApplicationContext context = Bootstrap.init();

        // ID организации (раньше был documentId)
        int orgId = 2;

        Organization org = context.organizationService().getById(orgId);
        if (org == null) {
            System.out.println("Организация не найдена");
            return;
        }

        var objects = context.objectService().getAllByOrgId(orgId);

        // ======================== TAG ============================
        byte[] template = Files.readAllBytes(Path.of(DocumentPathSet.TAG_TEMPLATE_PATH));
        for (ObjectModel object : objects) {
            WordprocessingMLPackage document =
                    context.wordGenerationService().generate(
                            FillStrategy.TAG,
                            template,
                            object.id()
                    );

            Path output = DocumentPathSet.buildOutputFile(org, object);
            document.save(output.toFile());
        }
        // ===================== PLACEHOLDER =======================
//      Не очень решение, много работы с параграфами если длинный текст вставки
//        byte[] placeholderTemplateBytes =
//                Files.readAllBytes(Path.of(DocumentPathUtil.PLACEHOLDER_TEMPLATE_PATH));
//
//        context.wordGenerationService().generate(
//                FillStrategy.PLACEHOLDER,
//                placeholderTemplateBytes,
//                documentSet
//        );
    }
}
