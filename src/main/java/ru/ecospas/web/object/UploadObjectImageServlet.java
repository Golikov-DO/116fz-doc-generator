package ru.ecospas.web.object;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.ObjectImage;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.repository.ObjectImageRepository;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
@MultipartConfig
public class UploadObjectImageServlet extends BaseServlet {

    private final ObjectImageRepository imageRepository;
    private final ObjectModelRepository objectRepository;

    public UploadObjectImageServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            ObjectImageRepository imageRepository,
            ObjectModelRepository objectRepository) {
        super(securityService, organizationRepository);
        this.imageRepository = imageRepository;
        this.objectRepository = objectRepository;
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

            ObjectModel object = objectRepository
                    .findById(objectId)
                    .orElse(null);

            if (object == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (requireAccess(req, resp, object.getOrganization().getId()) == null) {
                return;
            }

            List<ObjectImage> images = imageRepository.findAllByObjectId(objectId);

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

            imageRepository.save(image);

            resp.setContentType("application/json");
            resp.getWriter().write("{\"id\":" + image.getId() + "}");

        } catch (Exception e) {
            resp.setStatus(500);
            throw new ServletException(e);
        }
    }
}