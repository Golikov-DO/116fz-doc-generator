package com.caseo;

import com.caseo.app.ApplicationContext;
import com.caseo.app.Bootstrap;
import com.caseo.domain.model.DocumentSet;
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

        // какой документ генерируем
        int documentId = 3;

        DocumentSet documentSet = context.documentSetService().getById(documentId);
        Organization org = context.organizationService().getById(documentSet.orgId());

        var objects = context.objectService().getAllByOrgId(org.organizationId());

        // ======================== TAG ============================
        //Предпочтительный вариант очень гибкий и надёжный
        byte[] template = Files.readAllBytes(Path.of(DocumentPathSet.TAG_TEMPLATE_PATH));
        System.out.println("MAIN template size = " + template.length);
        for (ObjectModel object : objects) {

            DocumentSet perObject =
                    new DocumentSet(documentSet.id(), documentSet.orgId(), object.id());

            WordprocessingMLPackage document =
                    context.wordGenerationService().generate(
                            FillStrategy.TAG,
                            template,
                            perObject
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
