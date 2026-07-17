package ru.ecospas.domain.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.Asf;
import ru.ecospas.domain.model.AsfDocumentImage;
import ru.ecospas.domain.repository.AsfDocumentImageRepository;
import ru.ecospas.domain.repository.AsfRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Service
@RequiredArgsConstructor
@Transactional
public class AsfDocumentImageService {

    private final AsfRepository asfRepository;
    private final AsfDocumentImageRepository imageRepository;

    public Integer upload(HttpServletRequest req)
            throws IOException, ServletException {

        int asfId = paramInt(req, "asfId");
        String group = param(req, "group");
        int imageId = paramInt(req, "imageId");

        Part filePart = req.getPart("file");

        if (asfId == 0) {
            throw new ServletException("asfId is required");
        }

        if (group == null || group.isEmpty()) {
            throw new ServletException("group is required");
        }

        if (filePart == null || filePart.getSize() == 0) {
            throw new ServletException("file is required");
        }

        Asf asf = asfRepository.findById(asfId)
                .orElseThrow(() ->
                        new ServletException("Asf not found with id: " + asfId));

        byte[] data = filePart.getInputStream().readAllBytes();

        List<AsfDocumentImage> images =
                imageRepository.findAllByAsfId(asfId);

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

            image = images.stream()
                    .filter(i -> i.getId().equals(imageId))
                    .findFirst()
                    .orElseThrow(() ->
                            new ServletException("Image not found: " + imageId));

            image.setImageBlob(data);

        } else {

            image = new AsfDocumentImage();

            image.setAsf(asf);
            image.setGroupKey(group);

            String prefix =
                    "1".equals(group)
                            ? "Свидетельство "
                            : "Паспорт ";

            image.setNameDocument(prefix + nextNumber);
            image.setImageBlob(data);
        }

        imageRepository.save(image);

        return image.getId();
    }

    public void delete(Integer id) {
        imageRepository.deleteById(id);
    }

    public List<AsfDocumentImage> findAll(Integer asfId) {
        return imageRepository.findAllByAsfId(asfId);
    }

    //REST
    public Integer upload(
            Integer asfId,
            String group,
            Integer imageId,
            byte[] data
    ) {
        Asf asf = asfRepository.findById(asfId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Asf not found with id: " + asfId));
         List<AsfDocumentImage> images = imageRepository.findAllByAsfId(asfId);
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
            image = images.stream()
                    .filter(i -> i.getId().equals(imageId))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalArgumentException("Image not found: " + imageId));
            image.setImageBlob(data);
        } else {
            image = new AsfDocumentImage();
            image.setAsf(asf);
            image.setGroupKey(group);
            String prefix = "1".equals(group) ? "Свидетельство " : "Паспорт ";
            image.setNameDocument(prefix + nextNumber);
            image.setImageBlob(data);
        }
        imageRepository.save(image);
        return image.getId();
    }
}