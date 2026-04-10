package ru.ecospas.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import ru.ecospas.domain.model.Asf;
import ru.ecospas.domain.model.AsfDocumentImage;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;

import java.util.List;
import java.util.Objects;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@WebServlet("/upload-asf-image")
@MultipartConfig
public class UploadAsfImageServlet extends BaseServlet {

    private ParentService<Asf> asfService;
    private ChildService<AsfDocumentImage> imageService;

    @Override
    public void init() {
        super.init();
        asfService = services.getParentService(Asf.class);
        imageService = services.getChildService(AsfDocumentImage.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            int asfId = paramInt(req, "asfId");
            String group = param(req, "group");
            Part filePart = req.getPart("file");
            int imageId = paramInt(req, "imageId");

            if (asfId == 0) throw new ServletException("asfId is required");

            if (group == null || group.isEmpty()) throw new ServletException("group is required");

            if (filePart == null || filePart.getSize() == 0) throw new ServletException("file is required");

            Asf asf = asfService.getOneById(asfId);

            if (asf == null) throw new ServletException("Asf not found with id: " + asfId);

            byte[] data = filePart.getInputStream().readAllBytes();
            List<AsfDocumentImage> images = imageService.getManyByParentId(asfId);

            // Let's count how many images there are already in this group.
            int nextNumber = images.stream()
                    .filter(i -> group.equals(i.getGroupKey()))
                    .map(AsfDocumentImage::getNameDocument)
                    .filter(Objects::nonNull)
                    .map(n -> n.replaceAll("\\D+", ""))
                    .filter(s -> !s.isEmpty())
                    .mapToInt(Integer::parseInt)
                    .max()
                    .orElse(0) + 1;

            AsfDocumentImage image;

            if (imageId > 0) {
                // REPLACEMENT - looking for an existing one
                image = images.stream()
                        .filter(i -> i.getId() == imageId)
                        .findFirst()
                        .orElseThrow(() -> new ServletException("Image not found: " + imageId));

                // We ONLY update the picture, we DO NOT touch the name!
                image.setImageBlob(data);

            } else {
                // NEW - create
                image = new AsfDocumentImage();
                image.setGroupKey(group);
                image.setAsf(asf);

                // Generate a name only for the new one
                String prefix = "1".equals(group) ? "Свидетельство " : "Паспорт ";
                String imageName = prefix + nextNumber;
                image.setNameDocument(imageName);
                image.setImageBlob(data);
            }

            imageService.save(image);

            // Return the ID and generated name
            resp.setContentType("application/json");
            resp.getWriter().write("{\"id\":" + image.getId() + "}");

        } catch (Exception e) {
            getServletContext().log("Error loading ASF image", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new ServletException("Error loading ASF image", e);
        }
    }
}