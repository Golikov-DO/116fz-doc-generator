package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.Organization;
import com.caseo.domain.util.DocumentPathSet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@WebServlet("/downloadPlan")
public class DownloadPlanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String objectIdParam = req.getParameter("objectId");
        if (objectIdParam == null || objectIdParam.isEmpty()) {
            resp.sendError(400, "ID объекта обязателен");
            return;
        }

        int objectId = Integer.parseInt(objectIdParam);
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("appContext");
        InternalServices services = context.internalServices();

        ObjectModel object = services.getParentService(ObjectModel.class).getOneById(objectId);
        Organization org = services.getParentService(Organization.class).getOneById(object.getOrganization().getId());

        List<ObjectModel> objects = services.getChildService(ObjectModel.class)
                .getManyByParentId(org.getId());

        Path path = DocumentPathSet.buildOutputFile(org, object, objects);
        if (!Files.exists(path)) {
            resp.sendError(404, "Файл не найден");
            return;
        }

        resp.setContentType(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        );

        String fileName = path.getFileName().toString();

// для старых браузеров
        String safeName = fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

// для нормальных браузеров (UTF-8)
        String encoded = java.net.URLEncoder.encode(fileName, "UTF-8")
                .replaceAll("\\+", "%20");

        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + safeName + "\"; filename*=UTF-8''" + encoded);

        Files.copy(path, resp.getOutputStream());
    }
}