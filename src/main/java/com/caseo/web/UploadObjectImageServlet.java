package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.ObjectImage;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.service.ChildService;
import com.caseo.domain.service.ParentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.util.List;

import static com.caseo.web.util.RequestUtils.param;
import static com.caseo.web.util.RequestUtils.paramInt;

@WebServlet("/uploadObjectImage")
@MultipartConfig
public class UploadObjectImageServlet extends HttpServlet {

    private ChildService<ObjectImage> imageService;
    private ParentService<ObjectModel> objectService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();

        objectService = services.getParentService(ObjectModel.class);
        imageService = services.getChildService(ObjectImage.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int objectId = paramInt(req, "objectId");
            String group = param(req, "group");
            Part filePart = req.getPart("file");

            if (objectId == 0) throw new ServletException("objectId required");
            if (group == null) throw new ServletException("group required");

            byte[] data = filePart.getInputStream().readAllBytes();

            ObjectModel object = objectService.getOneById(objectId);

            List<ObjectImage> images = imageService.getManyByParentId(objectId);

            // Ищем существующее (1 картинка на группу!)
            ObjectImage image = images.stream()
                    .filter(i -> group.equals(i.getGroupKey()))
                    .findFirst()
                    .orElse(null);

            if (image == null) {
                image = new ObjectImage();
                image.setObject(object);
                image.setGroupKey(group);
            }

            image.setImageBlob(data);

            imageService.save(image);

            resp.setContentType("application/json");
            resp.getWriter().write("{\"id\":" + image.getId() + "}");

        } catch (Exception e) {
            resp.setStatus(500);
            throw new ServletException(e);
        }
    }
}