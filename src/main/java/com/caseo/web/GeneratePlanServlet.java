package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.Organization;
import com.caseo.domain.util.DocumentPathSet;
import com.caseo.word.strategy.FillStrategy;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@WebServlet("/generatePlan")
public class GeneratePlanServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. Устанавливаем временную папку СРАЗУ в начале
        String catalinaBase = System.getProperty("catalina.base");
        if (catalinaBase != null) {
            String tomcatTemp = catalinaBase + File.separator + "temp";
            File tempDir = new File(tomcatTemp);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            // Важно: для Docx4j нужно установить ОБА свойства
            System.setProperty("java.io.tmpdir", tomcatTemp);
            System.setProperty("docx4j.tmpdir", tomcatTemp);
            System.setProperty("org.docx4j.tmpdir", tomcatTemp);
        }

        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String orgIdParam = req.getParameter("orgId");

            if (orgIdParam == null || orgIdParam.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Organization ID is required");
                return;
            }

            int orgId = Integer.parseInt(orgIdParam);

            ApplicationContext context = (ApplicationContext) getServletContext()
                    .getAttribute("appContext");

            // Загружаем организацию
            Organization org = context.organizationService().getById(orgId);

            if (org == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Organization not found");
                return;
            }

            // Загружаем объекты организации
            List<ObjectModel> objects = context.objectService().getAllByOrgId(orgId);

            if (objects == null || objects.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No objects found for this organization");
                return;
            }

            String templatePath = getServletContext().getRealPath("/WEB-INF/template/tagtemplate.docx");

            if (templatePath == null) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Template file not found");
                return;
            }

            byte[] template = Files.readAllBytes(Paths.get(templatePath));

            int generatedCount = 0;
            for (ObjectModel object : objects) {

                WordprocessingMLPackage document = context.wordGenerationService().generate(
                        FillStrategy.TAG,
                        template,
                        object.id()  // только objectId!
                );

                Path outputPath = DocumentPathSet.buildOutputFile(org, object);
                document.save(outputPath.toFile());
                generatedCount++;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            out.println("План успешно разработан. Сгенерировано файлов: " + generatedCount);

        } catch (NumberFormatException e) {
            // ИСПРАВЛЕНО: текст ошибки соответствует параметру
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid organization ID format");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error generating plan: " + e.getMessage());
        }
    }
}