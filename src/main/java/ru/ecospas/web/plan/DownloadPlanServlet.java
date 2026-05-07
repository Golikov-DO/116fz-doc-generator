package ru.ecospas.web.plan;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.util.DocumentPathSet;
import ru.ecospas.web.BaseServlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("unused") // Managed via dynamic registration in ServletAutoRegistration
public class DownloadPlanServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String objectIdParam = req.getParameter("objectId");
        if (objectIdParam == null || objectIdParam.isEmpty()) {
            resp.sendError(400, "Object ID is required");
            return;
        }

        int objectId = Integer.parseInt(objectIdParam);
        super.init();
        ObjectModel object = services.getParentService(ObjectModel.class).getOneById(objectId);
        Organization org = services.getParentService(Organization.class).getOneById(object.getOrganization().getId());

        List<ObjectModel> objects = services.getChildService(ObjectModel.class).getManyByParentId(org.getId());

        Path path = DocumentPathSet.buildOutputFile(org, object, objects);
        if (!Files.exists(path)) {
            resp.sendError(404, "File not found");
            return;
        }

        resp.setContentType(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        );

        String fileName = path.getFileName().toString();

        // for older browsers
        String safeName = fileName.replaceAll("[^a-zA-Z0-9.\\-]", "_");

        // for modern browsers (UTF-8)
        String encoded = java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + safeName + "\"; filename*=UTF-8''" + encoded);

        Files.copy(path, resp.getOutputStream());
    }
}