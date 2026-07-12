package ru.ecospas.domain.service;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.util.DocumentPathSet;
import ru.ecospas.word.strategy.FillStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final ObjectModelRepository objectRepository;
    private final OrganizationRepository organizationRepository;
    private final WordGenerationService wordGenerationService;

    public void generate(
            ServletContext servletContext,
            HttpServletRequest req,
            Integer objectId
    ) throws Exception {

        String templatePath =
                servletContext.getRealPath("/WEB-INF/template/tagtemplate.docx");

        if (templatePath == null) {
            throw new IOException("Файл шаблона не найден");
        }

        byte[] template =
                Files.readAllBytes(Paths.get(templatePath));

        ObjectModel object = objectRepository.findById(objectId)
                .orElseThrow(() ->
                        new ServletException("Object not found"));

        Organization organization = object.getOrganization();

        WordprocessingMLPackage document =
                wordGenerationService.generate(
                        FillStrategy.TAG,
                        template,
                        objectId
                );

        List<ObjectModel> objects =
                objectRepository.findByOrganizationId(
                        organization.getId()
                );

        Path outputPath =
                DocumentPathSet.buildOutputFile(
                        organization,
                        object,
                        objects
                );

        File parentDir = outputPath.getParent().toFile();

        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        document.save(outputPath.toFile());
    }

    public void download(
            Integer objectId,
            HttpServletResponse resp
    ) throws IOException {

        ObjectModel object = objectRepository.findById(objectId)
                .orElseThrow();

        Organization organization =
                organizationRepository.findById(
                        object.getOrganization().getId()
                ).orElseThrow();

        List<ObjectModel> objects =
                objectRepository.findByOrganizationId(
                        organization.getId()
                );

        Path path =
                DocumentPathSet.buildOutputFile(
                        organization,
                        object,
                        objects
                );

        if (!Files.exists(path)) {
            throw new IOException("File not found");
        }

        resp.setContentType(
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        );

        String fileName = path.getFileName().toString();

        String safeName =
                fileName.replaceAll("[^a-zA-Z0-9.\\-]", "_");

        String encoded =
                java.net.URLEncoder.encode(
                        fileName,
                        StandardCharsets.UTF_8
                ).replaceAll("\\+", "%20");

        resp.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + safeName
                        + "\"; filename*=UTF-8''" + encoded
        );

        Files.copy(path, resp.getOutputStream());
    }
}