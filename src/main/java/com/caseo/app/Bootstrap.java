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

        // Родители
        parentRepos.put(Asf.class, new HibernateAsfRepository());
        parentRepos.put(ObjectHazardousSubstance.class, new HibernateObjectHazardousSubstanceRepository());
        parentRepos.put(Organization.class, new HibernateOrganizationRepository());
        parentRepos.put(ReferenceCity.class, new HibernateReferenceCityRepository());
        parentRepos.put(ReferenceEmergencyServices.class, new HibernateReferenceEmergencyServicesRepository());
        parentRepos.put(ReferenceTableTitle.class, new HibernateReferenceTableTitleRepository());

        // Дети (и те, кто и родитель и ребенок)
        childRepos.put(AsfCertificate.class, new HibernateAsfCertificateRepository());
        childRepos.put(AsfCompositionDeploymentFunds.class, new HibernateAsfCompositionDeploymentFundsRepository());
        childRepos.put(AsfDocumentImage.class, new HibernateAsfDocumentImageRepository());
        childRepos.put(AsfPersonnel.class, new HibernateAsfPersonnelRepository());
        childRepos.put(AsfSigner.class, new HibernateAsfSignerRepository());
        childRepos.put(AsfSpecialists.class, new HibernateAsfSpecialistRepository());
        childRepos.put(AsfWorkType.class, new HibernateAsfWorkTypeRepository());
        childRepos.put(ObjectAccidentScenarios.class, new HibernateObjectAccidentScenariosRepository());
        childRepos.put(ObjectAddress.class, new HibernateObjectAddressRepository());
        childRepos.put(ObjectCompositionKchs.class, new HibernateObjectCompositionKchsRepository());
        childRepos.put(ObjectFireEquipment.class, new HibernateObjectFireEquipmentRepository());
        childRepos.put(ObjectHazardousParam.class, new HibernateObjectHazardousParamRepository());
        childRepos.put(ObjectHazardousParamValue.class, new HibernateObjectHazardousParamValueRepository());
        childRepos.put(ObjectImage.class, new HibernateObjectImageRepository());
        childRepos.put(ObjectInsurancePolicy.class, new HibernateObjectInsurancePolicyRepository());
        childRepos.put(ObjectMainScenarios.class, new HibernateObjectMainScenariosRepository());
        childRepos.put(ObjectModel.class, new HibernateObjectRepository());
        childRepos.put(ObjectOrderMinimumBalance.class, new HibernateObjectOrderMinimumBalanceRepository());
        childRepos.put(ObjectPersonsResponsible.class, new HibernateObjectPersonsResponsibleRepository());
        childRepos.put(ObjectRegionalAuthorities.class, new HibernateObjectRegionAuthoritiesRepository());
        childRepos.put(ObjectStructure.class, new HibernateObjectStructureRepository());
        childRepos.put(ObjectTechnologicalBlock.class, new HibernateObjectTechnologicalBlockRepository());
        childRepos.put(ObjectTechnologicalEquipment.class, new HibernateObjectTechnologicalEquipmentRepository());
        childRepos.put(ObjectType.class, new HibernateObjectTypeRepository());
        childRepos.put(OrganizationAddress.class, new HibernateOrganizationAddressRepository());
        childRepos.put(OrganizationContact.class, new HibernateOrganizationContactRepository());
        childRepos.put(OrganizationSigner.class, new HibernateOrganizationSignerRepository());

        return new RepositoryContext(parentRepos, childRepos);
    }

    private static InternalServices initServices(RepositoryContext repoContext) {
        return new InternalServices(repoContext);
    }

    private static WordGenerationService initWordService(InternalServices services) {
        // Специфические сервисы для Word
        ObjectHazardService objectHazardService = new ObjectHazardService(
                services.getChildService(ObjectHazardousParam.class),
                services.getChildService(ObjectHazardousParamValue.class)
        );

        HazardTableLayoutService hazardTableLayoutService = new HazardTableLayoutService(
                services.getChildService(ObjectModel.class),
                objectHazardService
        );

        ContactTableLayoutService contactTableLayoutService = new ContactTableLayoutService(
                services.getChildService(ObjectModel.class),
                services.getParentService(ReferenceEmergencyServices.class),
                services.getChildService(ObjectRegionalAuthorities.class),
                services.getChildService(OrganizationContact.class),
                services.getParentService(Organization.class)
        );

        TextPlaceholderService textPlaceholderService = new TextPlaceholderService(services);

        UnifiedBlockFactory unifiedFactory = new UnifiedBlockFactory(
                new TableBlockFactory(
                        services.getChildService(ObjectCompositionKchs.class),
                        services.getChildService(ObjectModel.class),
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
                        services.getChildService(ObjectModel.class)
                ),
                new ListBlockFactory(
                        services.getChildService(ObjectModel.class),
                        services.getChildService(ObjectStructure.class),
                        services.getChildService(ObjectTechnologicalBlock.class),
                        services.getChildService(ObjectAddress.class),
                        services.getParentService(ReferenceCity.class)
                ),
                hazardTableLayoutService,
                contactTableLayoutService,
                services.getChildService(ObjectModel.class)
        );

        return new WordGenerationService(
                PipelineConfiguration.createDocumentBuilder(),
                PipelineConfiguration.createTagStrategy(unifiedFactory),
                PipelineConfiguration.createPlaceholderStrategy(unifiedFactory)
        );
    }
}