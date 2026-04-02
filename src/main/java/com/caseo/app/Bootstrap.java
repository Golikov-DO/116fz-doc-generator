package com.caseo.app;

import com.caseo.domain.model.*;
import com.caseo.domain.repository.ChildRepository;
import com.caseo.domain.repository.ParentRepository;
import com.caseo.domain.service.*;
import com.caseo.infrastructure.db.repository.*;
import com.caseo.word.blocks.text.TextPlaceholderService;
import com.caseo.word.factory.*;
import com.caseo.word.layout.ContactTableLayoutService;
import com.caseo.word.layout.HazardTableLayoutService;
import com.caseo.word.pipeline.PipelineConfiguration;
import com.caseo.word.strategy.PlaceholderFillStrategy;

import java.util.HashMap;
import java.util.Map;

public class Bootstrap {

    public static ApplicationContext init() {
        // 1. Инфраструктура (Репозитории)
        RepositoryContext repoContext = initRepositories();

        // 2. Бизнес-логика (Сервисы)
        InternalServices services = initServices(repoContext);

        // 3. Генератор документов
        var wordService = initWordService(services);

        return new ApplicationContext(wordService, services);
    }

    private static RepositoryContext initRepositories() {
        Map<Class<?>, ParentRepository<?>> parentRepos = new HashMap<>();
        Map<Class<?>, ChildRepository<?>> childRepos = new HashMap<>();

        // РОДИТЕЛИ
        parentRepos.put(Organization.class, new GenericHibernateRepository<>(Organization.class, null, "organizationShortName"));
        parentRepos.put(Asf.class, new GenericHibernateRepository<>(Asf.class, null, "shortName"));
        parentRepos.put(ReferenceHazardousSubstance.class, new GenericHibernateRepository<>(ReferenceHazardousSubstance.class, null, "name"));
        parentRepos.put(ReferenceCity.class, new GenericHibernateRepository<>(ReferenceCity.class, null, "cityName"));
        parentRepos.put(ReferenceEmergencyServices.class, new GenericHibernateRepository<>(ReferenceEmergencyServices.class, null, "id"));
        parentRepos.put(ReferenceTableTitle.class, new GenericHibernateRepository<>(ReferenceTableTitle.class, null, "id"));
        parentRepos.put(ObjectType.class, new GenericHibernateRepository<>(ObjectType.class, null, "id"));
        parentRepos.put(ObjectModel.class, new GenericHibernateRepository<>(ObjectModel.class, null, "id"));
        parentRepos.put(ReferenceHazardousParam.class, new GenericHibernateRepository<>(ReferenceHazardousParam.class, null, "id"));

        // СПЕЦИАЛЬНЫЙ СЛУЧАЙ: Объекты
        var objectRepo = new GenericHibernateRepository<>(ObjectModel.class, "organization", "id");
        parentRepos.put(ObjectModel.class, objectRepo);  // Регистрируем как родителя
        childRepos.put(ObjectModel.class, objectRepo); // Регистрируем как ребенка

        // ДЕТИ
        // ОДИНОЧНЫЕ ЗАПИСИ (OneToOne)
        childRepos.put(AsfCertificate.class, new GenericHibernateRepository<>(AsfCertificate.class, "asf", "id"  ));
        childRepos.put(AsfCompositionDeploymentFunds.class, new GenericHibernateRepository<>(AsfCompositionDeploymentFunds.class, "asf", "id"));
        childRepos.put(AsfPersonnel.class, new GenericHibernateRepository<>(AsfPersonnel.class, "asf", "id"));
        childRepos.put(AsfSpecialists.class, new GenericHibernateRepository<>(AsfSpecialists.class, "asf", "id"));
        childRepos.put(AsfWorkType.class, new GenericHibernateRepository<>(AsfWorkType.class, "asf", "id"));
        childRepos.put(ObjectAddress.class, new GenericHibernateRepository<>(ObjectAddress.class, "object", "id"));
        childRepos.put(ObjectInsurancePolicy.class, new GenericHibernateRepository<>(ObjectInsurancePolicy.class, "object", "id"));
        childRepos.put(ObjectFireEquipment.class, new GenericHibernateRepository<>(ObjectFireEquipment.class, "object", "id"));
        childRepos.put(ObjectOrderMinimumBalance.class, new GenericHibernateRepository<>(ObjectOrderMinimumBalance.class, "object", "id"));
        childRepos.put(ObjectPersonsResponsible.class, new GenericHibernateRepository<>(ObjectPersonsResponsible.class, "object", "id"));
        childRepos.put(ObjectRegionalAuthorities.class, new GenericHibernateRepository<>(ObjectRegionalAuthorities.class, "objectCity", "id"));
        childRepos.put(OrganizationAddress.class, new GenericHibernateRepository<>(OrganizationAddress.class, "organization", "id"));

        // СПИСКИ
        childRepos.put(AsfSigner.class, new GenericHibernateRepository<>(AsfSigner.class, "asf", "name"));
        childRepos.put(ObjectTechnologicalEquipment.class, new GenericHibernateRepository<>(ObjectTechnologicalEquipment.class, "object", "num"));
        childRepos.put(ObjectCompositionKchs.class, new GenericHibernateRepository<>(ObjectCompositionKchs.class, "object", "number"));
        childRepos.put(ObjectHazardousParamValue.class, new GenericHibernateRepository<>(ObjectHazardousParamValue.class, "substance", "id"));
        childRepos.put(OrganizationContact.class, new GenericHibernateRepository<>(OrganizationContact.class, "organization", "id"));
        childRepos.put(OrganizationSigner.class, new GenericHibernateRepository<>(OrganizationSigner.class, "organization", "id"));
        childRepos.put(AsfDocumentImage.class, new GenericHibernateRepository<>(AsfDocumentImage.class, "asf", "groupKey, id"));
        childRepos.put(ObjectStructure.class, new GenericHibernateRepository<>(ObjectStructure.class, "object", "id"));
        childRepos.put(ObjectTechnologicalBlock.class, new GenericHibernateRepository<>(ObjectTechnologicalBlock.class, "object", "num"));
        childRepos.put(ObjectAccidentScenarios.class, new GenericHibernateRepository<>(ObjectAccidentScenarios.class, "object", "id"));
        childRepos.put(ObjectImage.class, new GenericHibernateRepository<>(ObjectImage.class, "object", "groupKey, id"));
        childRepos.put(ObjectMainScenarios.class, new GenericHibernateRepository<>(ObjectMainScenarios.class, "object", "id"));

        return new RepositoryContext(parentRepos, childRepos);
    }

    private static InternalServices initServices(RepositoryContext repoContext) {
        return new InternalServices(repoContext);
    }

    private static WordGenerationService initWordService(InternalServices services) {
        // Специфические сервисы для Word
        ObjectHazardService objectHazardService = new ObjectHazardService(
                services.getParentService(ReferenceHazardousParam.class),
                services.getChildService(ObjectHazardousParamValue.class)
        );

        HazardTableLayoutService hazardTableLayoutService = new HazardTableLayoutService(
                services.getParentService(ObjectModel.class),
                objectHazardService
        );

        ContactTableLayoutService contactTableLayoutService = new ContactTableLayoutService(
                services.getParentService(ObjectModel.class),
                services.getParentService(ReferenceEmergencyServices.class),
                services.getChildService(ObjectRegionalAuthorities.class),
                services.getChildService(OrganizationContact.class),
                services.getParentService(Organization.class)
        );

        TextPlaceholderService textPlaceholderService = new TextPlaceholderService(services);

        UnifiedBlockFactory unifiedFactory = new UnifiedBlockFactory(
                new TableBlockFactory(
                        services.getChildService(ObjectCompositionKchs.class),
                        services.getParentService(ObjectModel.class),
                        services.getChildService(ObjectTechnologicalEquipment.class),
                        services.getChildService(ObjectAccidentScenarios.class),
                        services.getChildService(ObjectMainScenarios.class),
                        services.getChildService(ObjectFireEquipment.class),
                        services.getChildService(ObjectPersonsResponsible.class)
                ),
                new PlaceholderFillStrategy(textPlaceholderService),
                new ImageBlockFactory(
                        services.getChildService(ObjectImage.class),
                        services.getChildService(AsfDocumentImage.class),
                        services.getParentService(ObjectModel.class)
                ),
                new ListBlockFactory(
                        services.getParentService(ObjectModel.class),
                        services.getChildService(ObjectStructure.class),
                        services.getChildService(ObjectTechnologicalBlock.class),
                        services.getChildService(ObjectAddress.class),
                        services.getParentService(ReferenceCity.class)
                ),
                hazardTableLayoutService,
                contactTableLayoutService,
                services.getParentService(ObjectModel.class)
        );

        return new WordGenerationService(
                PipelineConfiguration.createDocumentBuilder(),
                PipelineConfiguration.createTagStrategy(unifiedFactory),
                PipelineConfiguration.createPlaceholderStrategy(unifiedFactory)
        );
    }
}