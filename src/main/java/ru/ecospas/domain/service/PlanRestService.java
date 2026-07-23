package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.hibernate.Hibernate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.model.Organization;
import ru.ecospas.domain.repository.ObjectModelRepository;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.util.DocumentPathSet;
import ru.ecospas.word.strategy.FillStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanRestService {

    private final ObjectModelRepository objectRepository;
    private final OrganizationRepository organizationRepository;
    private final WordGenerationService wordGenerationService;
    private final ResourceLoader resourceLoader;

    @Transactional(readOnly = true)
    public Path generate(Integer objectId) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:template/tagtemplate.docx");
        byte[] template = resource.getInputStream().readAllBytes();

        // Загружаем и инициализируем объект со ВСЕМИ коллекциями
        ObjectModel object = loadObjectWithCollections(objectId);

        Organization organization = object.getOrganization();

        // Передаём уже инициализированный объект в генерацию
        WordprocessingMLPackage document = wordGenerationService.generate(
                FillStrategy.TAG,
                template,
                object  // ← передаём объект, а не id
        );

        List<ObjectModel> objects = objectRepository.findByOrganizationId(organization.getId());

        Path outputPath = DocumentPathSet.buildOutputFile(organization, object, objects);

        Files.createDirectories(outputPath.getParent());

        document.save(outputPath.toFile());
        return outputPath;
    }

    @Transactional(readOnly = true)
    public File getPlanFile(Integer objectId) throws IOException {
        ObjectModel object = objectRepository.findById(objectId)
                .orElseThrow(() -> new IllegalArgumentException("Object not found"));

        Organization organization = organizationRepository.findById(object.getOrganization().getId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        List<ObjectModel> objects = objectRepository.findByOrganizationId(organization.getId());

        Path path = DocumentPathSet.buildOutputFile(organization, object, objects);

        if (!Files.exists(path)) {
            throw new IOException("File not found");
        }

        return path.toFile();
    }

    private ObjectModel loadObjectWithCollections(Integer objectId) {
        ObjectModel object = objectRepository.findById(objectId)
                .orElseThrow(() -> new IllegalArgumentException("Object not found"));

        // Инициализируем ВСЕ ленивые коллекции и связи
        Hibernate.initialize(object.getImages());
        Hibernate.initialize(object.getStructures());
        Hibernate.initialize(object.getTechnologicalBlocks());
        Hibernate.initialize(object.getTechnologicalEquipments());
        Hibernate.initialize(object.getFireEquipments());
        Hibernate.initialize(object.getCompositionKchs());
        Hibernate.initialize(object.getResponsiblePersons());

        if (object.getAddress() != null) Hibernate.initialize(object.getAddress());
        if (object.getInsurancePolicy() != null) Hibernate.initialize(object.getInsurancePolicy());
        if (object.getMinimumBalance() != null) Hibernate.initialize(object.getMinimumBalance());
        if (object.getType() != null) Hibernate.initialize(object.getType());
        if (object.getCity() != null) Hibernate.initialize(object.getCity());
        if (object.getHazardousSubstance() != null) Hibernate.initialize(object.getHazardousSubstance());

        if (object.getOrganization() != null) {
            Hibernate.initialize(object.getOrganization());
            if (object.getOrganization().getAddress() != null) {
                Hibernate.initialize(object.getOrganization().getAddress());
            }
            if (object.getOrganization().getSigners() != null) {
                Hibernate.initialize(object.getOrganization().getSigners());
            }
        }

        if (object.getAsf() != null) {
            Hibernate.initialize(object.getAsf());
            if (object.getAsf().getCertificate() != null) {
                Hibernate.initialize(object.getAsf().getCertificate());
            }
            if (object.getAsf().getPersonnel() != null) {
                Hibernate.initialize(object.getAsf().getPersonnel());
            }
            if (object.getAsf().getSpecialists() != null) {
                Hibernate.initialize(object.getAsf().getSpecialists());
            }
            if (object.getAsf().getDeployment() != null) {
                Hibernate.initialize(object.getAsf().getDeployment());
            }
            if (object.getAsf().getSigners() != null) {
                Hibernate.initialize(object.getAsf().getSigners());
            }
            if (object.getAsf().getWorkTypes() != null) {
                Hibernate.initialize(object.getAsf().getWorkTypes());
            }
            if (object.getAsf().getImages() != null) {
                Hibernate.initialize(object.getAsf().getImages());
            }
        }

        return object;
    }
}