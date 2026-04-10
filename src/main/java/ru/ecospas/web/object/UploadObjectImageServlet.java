package ru.ecospas.web.object;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import ru.ecospas.domain.model.ObjectImage;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.web.BaseServlet;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@SuppressWarnings("unused") // Managed via dynamic registration in ServletAutoRegistration
@MultipartConfig
public class UploadObjectImageServlet extends BaseServlet {

    private ChildService<ObjectImage> imageService;
    private ParentService<ObjectModel> objectService;

    @Override
    public void init() {
        super.init();

        objectService = services.getParentService(ObjectModel.class);
        imageService = services.getChildService(ObjectImage.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            int objectId = paramInt(req, "objectId");
            String group = param(req, "group");
            Part filePart = req.getPart("file");

            if (objectId == 0) throw new ServletException("objectId required");
            if (group == null) throw new ServletException("group required");

            byte[] data = filePart.getInputStream().readAllBytes();

            ObjectModel object = objectService.getOneById(objectId);

            List<ObjectImage> images = imageService.getManyByParentId(objectId);

            // Looking for an existing one (1 picture per group!)
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