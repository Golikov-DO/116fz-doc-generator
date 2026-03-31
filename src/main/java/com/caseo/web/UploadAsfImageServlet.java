package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.Asf;
import com.caseo.domain.model.AsfDocumentImage;
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
import java.util.Objects;

import static com.caseo.web.util.RequestUtils.param;
import static com.caseo.web.util.RequestUtils.paramInt;

@WebServlet("/uploadAsfImage")
@MultipartConfig
public class UploadAsfImageServlet extends HttpServlet {

    private ParentService<Asf> asfService;
    private ChildService<AsfDocumentImage> imageService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();

        asfService = services.getParentService(Asf.class);
        imageService = services.getChildService(AsfDocumentImage.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int asfId = paramInt(req, "asfId");
            String group = param(req, "group");
            Part filePart = req.getPart("file");
            int imageId = paramInt(req, "imageId");

            if (asfId == 0) {
                throw new ServletException("asfId is required");
            }

            if (group == null || group.isEmpty()) {
                throw new ServletException("group is required");
            }

            if (filePart == null || filePart.getSize() == 0) {
                throw new ServletException("file is required");
            }

            Asf asf = asfService.getOneById(asfId);
            if (asf == null) {
                throw new ServletException("Asf not found with id: " + asfId);
            }

            byte[] data = filePart.getInputStream().readAllBytes();
            List<AsfDocumentImage> images = imageService.getManyByParentId(asfId);

            // Считаем сколько уже есть изображений в этой группе
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
                // ЗАМЕНА - ищем существующее
                image = images.stream()
                        .filter(i -> i.getId() == imageId)
                        .findFirst()
                        .orElseThrow(() -> new ServletException("Image not found: " + imageId));

                // ТОЛЬКО обновляем картинку, имя НЕ трогаем!
                image.setImageBlob(data);
                // НЕ вызываем setNameDocument() - оставляем как было

            } else {
                // НОВОЕ - создаем
                image = new AsfDocumentImage();
                image.setGroupKey(group);
                image.setAsf(asf);

                // Генерируем имя только для нового
                String prefix = "1".equals(group) ? "Свидетельство " : "Паспорт ";
                String imageName = prefix + nextNumber;
                image.setNameDocument(imageName);
                image.setImageBlob(data);
            }

            imageService.save(image);

            // Возвращаем ID и сгенерированное имя
            resp.setContentType("application/json");
            resp.getWriter().write("{\"id\":" + image.getId() + "}");

        } catch (Exception e) {
            getServletContext().log("Ошибка при загрузке изображения АСФ", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new ServletException("Ошибка при загрузке изображения АСФ", e);
        }
    }
}