package com.caseo;

import com.caseo.app.ApplicationContext;
import com.caseo.app.Bootstrap;
import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.Organization;
import com.caseo.domain.util.DocumentPathUtil;
import com.caseo.word.strategy.FillStrategy;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws Exception {

        ApplicationContext context = Bootstrap.init();

        // какой документ генерируем
        int documentId = 2;

        DocumentSet documentSet = context.documentSetService().getById(documentId);
        Organization org = context.organizationService().getById(documentSet.id());

        // ======================== TAG ============================
        //Предпочтительный вариант очень гибкий и надёжный
        byte[] tagTemplateBytes =
                Files.readAllBytes(Path.of(DocumentPathUtil.TAG_TEMPLATE_PATH));

        WordprocessingMLPackage document =
        context.wordGenerationService().generate(
                FillStrategy.TAG,
                tagTemplateBytes,
                documentSet
        );

        document.save(DocumentPathUtil.buildOutputFile(org));

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
