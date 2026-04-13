package ru.ecospas.web.plan;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import ru.ecospas.app.ApplicationContext;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.util.DocumentPathSet;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.word.strategy.FillStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SuppressWarnings("unused") // Managed via dynamic registration in ServletAutoRegistration
public class GeneratePlanServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // 1. First, we check the ID without opening any streams (PrintWriter)
        String objectIdParam = req.getParameter("objectId");
        if (objectIdParam == null || objectIdParam.isEmpty()) {
            resp.sendError(400, "Object ID is required");
            return;
        }

        try {
            int objectId = Integer.parseInt(objectIdParam);
            ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("appContext");
            super.init();

            // 2. Read the template ONCE before the cycle
            String templatePath = getServletContext().getRealPath("/WEB-INF/template/tagtemplate.docx");
            if (templatePath == null) throw new IOException("Файл шаблона не найден");
            byte[] template = Files.readAllBytes(Paths.get(templatePath));

            // 3. Loading data
            ObjectModel object = services.getParentService(ObjectModel.class).getOneById(objectId);
            Organization org = services.getParentService(Organization.class).getOneById(object.getOrganization().getId());

            // 4. Generation
            WordprocessingMLPackage document = context.wordGenerationService().generate(
                    FillStrategy.TAG, template, object.getId()
            );

            List<ObjectModel> objects = services.getChildService(ObjectModel.class)
                    .getManyByParentId(org.getId());

            Path outputPath = DocumentPathSet.buildOutputFile(org, object, objects);
            File parentDir = outputPath.getParent().toFile();

            if (!parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (!created) {
                    getServletContext().log("Не удалось создать директорию: " + parentDir.getAbsolutePath());
                }
            }

            document.save(outputPath.toFile());

            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().println("План успешно разработан");

        } catch (Exception e) {
            getServletContext().log("Generation Word document error", e);
            throw new ServletException("Generation Word document error", e);
        }
    }
}