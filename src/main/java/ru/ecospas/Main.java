package ru.ecospas;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import ru.ecospas.app.ApplicationContext;
import ru.ecospas.app.Bootstrap;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.domain.util.DocumentPathSet;
import ru.ecospas.word.strategy.FillStrategy;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {


    public static void main(String[] args) throws Exception {

        ApplicationContext context = Bootstrap.init();

        int orgId = 6;

        InternalServices services = context.internalServices();

        ParentService<Organization> organizationService = services.getParentService(Organization.class);
        ChildService<ObjectModel> objectService = services.getChildService(ObjectModel.class);


        Organization org = organizationService.getOneById(orgId);
        if (org == null) {
            System.out.println("Организация не найдена");
            return;
        }

        var objects = objectService.getManyByParentId(orgId);

        // ======================== TAG ============================
        byte[] template = Files.readAllBytes(Path.of(DocumentPathSet.TAG_TEMPLATE_PATH));
        for (ObjectModel object : objects) {
            WordprocessingMLPackage document =
                    context.wordGenerationService().generate(
                            FillStrategy.TAG,
                            template,
                            object.getId()
                    );

            Path output = DocumentPathSet.buildOutputFile(org, object, objects);
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
