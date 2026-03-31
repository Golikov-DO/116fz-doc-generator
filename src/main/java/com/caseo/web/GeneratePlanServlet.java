package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.Organization;
import com.caseo.domain.util.DocumentPathSet;
import com.caseo.word.strategy.FillStrategy;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@WebServlet("/generatePlan")
public class GeneratePlanServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 1. Сначала проверяем ID, не открывая никаких потоков (PrintWriter)
        String objectIdParam = req.getParameter("objectId");
        if (objectIdParam == null || objectIdParam.isEmpty()) {
            resp.sendError(400, "ID объекта обязателен");
            return;
        }

        try {
            int objectId = Integer.parseInt(objectIdParam);
            ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("appContext");
            InternalServices services = context.internalServices();

            // 2. Читаем шаблон ОДИН РАЗ перед циклом
            String templatePath = getServletContext().getRealPath("/WEB-INF/template/tagtemplate.docx");
            if (templatePath == null) throw new IOException("Файл шаблона не найден");
            byte[] template = Files.readAllBytes(Paths.get(templatePath));

            // 3. Загружаем данные
            ObjectModel object = services.getParentService(ObjectModel.class).getOneById(objectId);
            Organization org = services.getParentService(Organization.class).getOneById(object.getOrganization().getId());

            // 4. Генерация
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
            e.printStackTrace();

            getServletContext().log("Ошибка генерации", e);

            if (!resp.isCommitted()) {
                resp.setContentType("text/plain;charset=UTF-8");
                resp.getWriter().println("Ошибка: " + e.getMessage());
            }
        }
    }
}